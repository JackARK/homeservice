package club.saltfish.homeservice.server

import android.provider.Settings
import club.saltfish.homeservice.App
import club.saltfish.homeservice.config.ConfigManager
import club.saltfish.homeservice.config.Redaction
import club.saltfish.homeservice.log.RingLogBuffer
import club.saltfish.homeservice.notification.RecentEvents
import club.saltfish.homeservice.root.RootShell
import club.saltfish.homeservice.rule.ActionDef
import club.saltfish.homeservice.rule.Rule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.IOException

/**
 * 内嵌 HTTP 服务器（NanoHTTPD），同时承担两个角色：
 *
 * 1. **静态托管**：GET 的非 API 路径从 `assets/web/` 返回 Web 管理端 SPA
 *    （`/` → index.html，无扩展名路径回退 index.html，拒绝 `..` 路径穿越），免鉴权。
 * 2. **REST API**：统一 `/api/` 前缀；旧根路径（/rules、/config、/tts、/action、/health）
 *    保留为别名，行为一致。
 *
 * API 端点：
 * - GET  /api/health  健康检查（免鉴权，只返回 {"status":"ok"}，不泄漏任何其它信息）
 * - GET  /api/status  运行状态看板（uptime、版本、root、监听授权、bridge/HA 连通性、最近通知事件）
 * - GET  /api/logs    日志查询（level/keyword/afterId/limit，afterId 为增量轮询游标）
 * - GET  /rules       当前规则
 * - POST /rules       更新规则（热更新，持久化 + 重载）
 * - GET  /config      当前配置（**敏感字段脱敏返回**，见 [Redaction]）
 * - POST /config      更新配置（掩码字段自动还原为旧值）
 * - GET  /tts         当前音色 + 可用音色列表（current / voices）
 * - POST /tts         单独修改音色（body: {"speaker":"<音色ID>"}），无需整包提交配置
 * - POST /action      手动触发动作（body: 动作 JSON 数组）
 *
 * 鉴权：除 /api/health 外的所有 API 都要求 token（query 参数 `token` 或
 * `Authorization: Bearer`）。token 在每次请求时动态读取 [App.serverToken]——
 * 配置热更新 token 后**即时生效，无需重启服务器**（端口变更由 [App] 负责换实例）。
 * 同一 IP 连续鉴权失败会被 [AuthRateLimiter] 临时锁定（内网穿透后防爆破）。
 */
class HttpServer(
    private val app: App,
    port: Int,
    private val logBuffer: RingLogBuffer,
    private val rateLimiter: AuthRateLimiter = AuthRateLimiter()
) : NanoHTTPD(port) {

    private val gson = Gson()

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.let { if (it.length > 1) it.trimEnd('/') else it }
        return try {
            when {
                uri.startsWith("/api") -> serveApi(session, uri)
                isLegacyApi(session.method, uri) -> serveApi(session, uri)
                session.method == Method.GET -> serveStatic(uri)
                else -> json(Response.Status.NOT_FOUND, mapOf("error" to "not found: ${session.method} $uri"))
            }
        } catch (e: Exception) {
            Timber.w(e, "HTTP 处理异常: ${session.method} $uri")
            json(Response.Status.INTERNAL_ERROR, mapOf("error" to (e.message ?: "internal error")))
        }
    }

    /** 旧根路径端点（/api 前缀引入前的别名，保持兼容） */
    private fun isLegacyApi(method: Method, uri: String): Boolean = when (method) {
        Method.GET -> uri in listOf("/health", "/rules", "/config", "/tts")
        Method.POST -> uri in listOf("/rules", "/config", "/tts", "/action")
        else -> false
    }

    private fun serveApi(session: IHTTPSession, uri: String): Response {
        val path = uri.removePrefix("/api").ifBlank { "/" }
        val method = session.method

        // 健康检查免鉴权（供穿透层探活），仅返回最小信息
        if (method == Method.GET && path == "/health") {
            return json(Response.Status.OK, mapOf("status" to "ok"))
        }

        authError(session)?.let { return it }

        return when {
            method == Method.GET && path == "/status" -> status()
            method == Method.GET && path == "/logs" -> logs(session)
            method == Method.GET && path == "/rules" -> getRules()
            method == Method.POST && path == "/rules" -> updateRules(session)
            method == Method.GET && path == "/config" -> getConfig()
            method == Method.POST && path == "/config" -> updateConfig(session)
            method == Method.GET && path == "/tts" -> getTts()
            method == Method.POST && path == "/tts" -> updateTts(session)
            method == Method.POST && path == "/action" -> postAction(session)
            else -> json(Response.Status.NOT_FOUND, mapOf("error" to "not found: $method $uri"))
        }
    }

    /** 鉴权（含限流）。返回 null 表示通过；否则返回应直接响应的错误 Response */
    private fun authError(session: IHTTPSession): Response? {
        val ip = session.remoteIpAddress ?: "unknown"
        if (rateLimiter.isLocked(ip)) {
            Timber.w("IP $ip 鉴权失败次数过多，已临时锁定")
            val status = Response.Status.lookup(429) ?: Response.Status.FORBIDDEN
            return json(status, mapOf("error" to "too many failures, locked"))
        }
        // token 动态读取：配置热更新后下一个请求即用新 token，无需重启服务器
        val expected = app.serverToken.orEmpty()
        val param = session.parameters["token"]?.firstOrNull()
        val header = session.headers["authorization"]
        val ok = expected.isNotBlank() && (param == expected || header == "Bearer $expected")
        if (ok) {
            rateLimiter.onSuccess(ip)
            return null
        }
        rateLimiter.onFailure(ip)
        Timber.w("鉴权失败: ip=$ip uri=${session.uri}")
        return json(Response.Status.UNAUTHORIZED, mapOf("error" to "unauthorized"))
    }

    /** 状态看板：bridge/HA 连通性并行探测，单次请求总耗时取决于较慢的一方 */
    private fun status(): Response {
        val (bridgeOk, haOk) = runBlocking {
            val b = async(Dispatchers.IO) { app.bridgeClient.health() }
            val h = async(Dispatchers.IO) { app.haClient.health() }
            b.await() to h.await()
        }
        val listeners = try {
            Settings.Secure.getString(app.contentResolver, "enabled_notification_listeners").orEmpty()
        } catch (e: Exception) {
            Timber.w(e, "读取通知监听授权状态失败")
            ""
        }
        val versionName = try {
            app.packageManager.getPackageInfo(app.packageName, 0).versionName ?: "unknown"
        } catch (e: Exception) {
            Timber.w(e, "读取版本号失败")
            "unknown"
        }
        return json(
            Response.Status.OK,
            mapOf(
                "status" to "ok",
                "uptimeMs" to (System.currentTimeMillis() - app.startTimeMillis),
                "version" to versionName,
                "rootAvailable" to RootShell.isAvailable(),
                "listenerEnabled" to listeners.contains(app.packageName),
                "ruleCount" to app.config.rules.size,
                "bridgeReachable" to bridgeOk,
                "haReachable" to haOk,
                "serverPort" to app.config.server.port,
                "events" to RecentEvents.list()
            )
        )
    }

    /** 日志查询：level 为 android.util.Log 优先级（如 4=INFO），afterId 增量轮询 */
    private fun logs(session: IHTTPSession): Response {
        val params = session.parameters
        val level = params["level"]?.firstOrNull()?.toIntOrNull()
        val keyword = params["keyword"]?.firstOrNull()
        val afterId = params["afterId"]?.firstOrNull()?.toLongOrNull()
        val limit = params["limit"]?.firstOrNull()?.toIntOrNull()?.coerceIn(1, 1000) ?: 200
        val (entries, lastId) = logBuffer.query(level, keyword, afterId, limit)
        return json(Response.Status.OK, mapOf("entries" to entries, "lastId" to lastId))
    }

    // ---------- 静态资源托管（assets/web/ 下的 SPA） ----------

    private fun serveStatic(uri: String): Response {
        if (uri.contains("..")) {
            return json(Response.Status.FORBIDDEN, mapOf("error" to "forbidden"))
        }
        val path = if (uri.isEmpty() || uri == "/") "/index.html" else uri
        readAsset("web$path")?.let { return fixed(Response.Status.OK, mimeFor(path), it) }
        // SPA 路由回退：仅对无扩展名的路径回退 index.html
        if (!path.substringAfterLast('/').contains('.')) {
            readAsset("web/index.html")?.let { return fixed(Response.Status.OK, "text/html; charset=utf-8", it) }
        }
        return json(Response.Status.NOT_FOUND, mapOf("error" to "not found: $path"))
    }

    private fun readAsset(path: String): ByteArray? = try {
        app.assets.open(path).use { it.readBytes() }
    } catch (e: IOException) {
        null // 文件不存在属正常探测（SPA 回退），不打日志
    }

    private fun mimeFor(path: String): String = when (path.substringAfterLast('.', "").lowercase()) {
        "html" -> "text/html; charset=utf-8"
        "js", "mjs" -> "application/javascript; charset=utf-8"
        "css" -> "text/css; charset=utf-8"
        "json", "map" -> "application/json; charset=utf-8"
        "svg" -> "image/svg+xml"
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "ico" -> "image/x-icon"
        "woff" -> "font/woff"
        "woff2" -> "font/woff2"
        "ttf" -> "font/ttf"
        "txt" -> "text/plain; charset=utf-8"
        else -> "application/octet-stream"
    }

    // ---------- 既有端点实现 ----------

    private fun getRules(): Response = json(Response.Status.OK, app.config.rules)

    private fun updateRules(session: IHTTPSession): Response {
        val rules: List<Rule> = gson.fromJson(readBody(session), object : TypeToken<List<Rule>>() {}.type)
        ConfigManager.save(app, app.config.copy(rules = rules))
        app.reloadConfig()
        return json(Response.Status.OK, mapOf("status" to "updated", "rules" to rules.size))
    }

    /** 读取配置：敏感字段脱敏返回（防止经内网穿透泄漏 token/apiKey） */
    private fun getConfig(): Response = json(Response.Status.OK, Redaction.redact(app.config))

    /** 更新配置：仍为掩码的敏感字段还原为旧值（前端留空不动 = 不修改） */
    private fun updateConfig(session: IHTTPSession): Response {
        val merged = Redaction.mergeSecrets(ConfigManager.parse(readBody(session)), app.config)
        ConfigManager.save(app, merged)
        app.reloadConfig()
        // server.port / server.token 变更由 reloadConfig 检测并自动重启内嵌服务器（延迟约 0.5s）
        return json(Response.Status.OK, mapOf("status" to "updated"))
    }

    /** 查询当前音色 + 可用音色列表（列表来自 bridge 的火山音色库；拉取失败时 voices 为空，不影响读取当前值） */
    private fun getTts(): Response {
        val voices = runBlocking { app.bridgeClient.listVoices() }.getOrElse {
            Timber.w(it, "拉取音色列表失败，仅返回当前音色")
            emptyMap()
        }
        return json(
            Response.Status.OK,
            mapOf("current" to app.config.bridge.ttsSpeaker, "voices" to voices)
        )
    }

    /** 单独修改音色：body {"speaker":"<音色ID>"}（也兼容 "ttsSpeaker" 字段），保存并热重载 */
    private fun updateTts(session: IHTTPSession): Response {
        val body = gson.fromJson(readBody(session), Map::class.java)
        val speaker = (body?.get("speaker") ?: body?.get("ttsSpeaker")) as? String
        if (speaker.isNullOrBlank()) {
            return json(Response.Status.BAD_REQUEST, mapOf("error" to "missing field: speaker"))
        }
        ConfigManager.save(app, app.config.copy(bridge = app.config.bridge.copy(ttsSpeaker = speaker)))
        app.reloadConfig()
        Timber.i("音色已更新为 $speaker")
        return json(Response.Status.OK, mapOf("status" to "updated", "current" to speaker))
    }

    private fun postAction(session: IHTTPSession): Response {
        val actions: List<ActionDef> = gson.fromJson(readBody(session), object : TypeToken<List<ActionDef>>() {}.type)
        app.applicationScope.launch { app.actionDispatcher.dispatch(actions) }
        return json(Response.Status.OK, mapOf("status" to "dispatched", "count" to actions.size))
    }

    /** 读取请求 body（按 content-length 从输入流读取，强制 UTF-8 解码以正确处理中文） */
    private fun readBody(session: IHTTPSession): String {
        val len = session.headers["content-length"]?.toIntOrNull() ?: 0
        if (len <= 0) return ""
        val buffer = ByteArray(len)
        var read = 0
        while (read < len) {
            val r = session.inputStream.read(buffer, read, len - read)
            if (r < 0) break
            read += r
        }
        return String(buffer, 0, read, Charsets.UTF_8)
    }

    private fun json(status: Response.Status, payload: Any): Response =
        newFixedLengthResponse(status, "application/json; charset=utf-8", gson.toJson(payload))

    private fun fixed(status: Response.Status, mime: String, bytes: ByteArray): Response =
        newFixedLengthResponse(status, mime, ByteArrayInputStream(bytes), bytes.size.toLong())
}
