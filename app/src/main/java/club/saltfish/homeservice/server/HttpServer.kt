package club.saltfish.homeservice.server

import club.saltfish.homeservice.App
import club.saltfish.homeservice.config.ConfigManager
import club.saltfish.homeservice.rule.ActionDef
import club.saltfish.homeservice.rule.Rule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.HashMap

/**
 * 内嵌 HTTP 服务器（NanoHTTPD）。
 *
 * 端点：
 * - GET  /health  健康检查
 * - GET  /rules   当前规则
 * - POST /rules   更新规则（热更新，持久化 + 重载）
 * - GET  /config  当前完整配置
 * - POST /config  更新完整配置
 * - GET  /tts     当前音色 + 可用音色列表（current / voices）
 * - POST /tts     单独修改音色（body: {"speaker":"<音色ID>"}），无需整包提交配置
 * - POST /action  手动触发动作（body: 动作 JSON 数组）
 *
 * 鉴权：token 非空时要求请求附带相同 token（query 参数 `token` 或 `Authorization: Bearer`）。
 */
class HttpServer(
    private val app: App,
    port: Int,
    private val token: String
) : NanoHTTPD(port) {

    private val gson = Gson()

    override fun serve(session: IHTTPSession): Response {
        if (!checkAuth(session)) {
            return json(Response.Status.UNAUTHORIZED, mapOf("error" to "unauthorized"))
        }
        val method = session.method
        val uri = session.uri.trimEnd('/')
        return try {
            when {
                method == Method.GET && uri == "/health" -> health()
                method == Method.GET && uri == "/rules" -> getRules()
                method == Method.POST && uri == "/rules" -> updateRules(session)
                method == Method.GET && uri == "/config" -> getConfig()
                method == Method.POST && uri == "/config" -> updateConfig(session)
                method == Method.GET && uri == "/tts" -> getTts()
                method == Method.POST && uri == "/tts" -> updateTts(session)
                method == Method.POST && uri == "/action" -> postAction(session)
                else -> json(Response.Status.NOT_FOUND, mapOf("error" to "not found: $method $uri"))
            }
        } catch (e: Exception) {
            Timber.w(e, "HTTP 处理异常: $method $uri")
            json(Response.Status.INTERNAL_ERROR, mapOf("error" to (e.message ?: "internal error")))
        }
    }

    private fun checkAuth(session: IHTTPSession): Boolean {
        if (token.isBlank()) return true
        val param = session.parameters["token"]?.firstOrNull()
        if (param == token) return true
        val header = session.headers["authorization"]
        if (header == "Bearer $token") return true
        return false
    }

    private fun health(): Response =
        json(Response.Status.OK, mapOf("status" to "ok", "rules" to app.config.rules.size))

    private fun getRules(): Response = json(Response.Status.OK, app.config.rules)

    private fun updateRules(session: IHTTPSession): Response {
        val rules: List<Rule> = gson.fromJson(readBody(session), object : TypeToken<List<Rule>>() {}.type)
        ConfigManager.save(app, app.config.copy(rules = rules))
        app.reloadConfig()
        return json(Response.Status.OK, mapOf("status" to "updated", "rules" to rules.size))
    }

    private fun getConfig(): Response = json(Response.Status.OK, app.config)

    private fun updateConfig(session: IHTTPSession): Response {
        ConfigManager.save(app, ConfigManager.parse(readBody(session)))
        app.reloadConfig()
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
}
