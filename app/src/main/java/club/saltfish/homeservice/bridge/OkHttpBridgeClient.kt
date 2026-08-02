package club.saltfish.homeservice.bridge

import club.saltfish.homeservice.config.BridgeConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * 基于 OkHttp 的 [BridgeClient] 实现。
 *
 * - 超时：[BridgeConfig.timeoutMs]（连接/读/写统一）
 * - 重试：[BridgeConfig.retry] 次，指数退避（1s → 2s → 4s，上限 8s）
 * - 鉴权：[BridgeConfig.token] 非空时附带 `Authorization: Bearer <token>`
 */
class OkHttpBridgeClient(private val config: BridgeConfig) : BridgeClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .build()

    private val jsonMedia = "application/json; charset=utf-8".toMediaType()
    private val gson = Gson()

    override suspend fun playText(text: String): Result<Unit> =
        post("/api/play/text", gson.toJson(mapOf("text" to text)))

    override suspend fun playUrl(url: String): Result<Unit> =
        post("/api/play/url", gson.toJson(mapOf("url" to url)))

    override suspend fun wakeup(): Result<Unit> = post("/api/wakeup", "{}")

    override suspend fun interrupt(): Result<Unit> = post("/api/interrupt", "{}")

    override suspend fun health(): Boolean = withContext(Dispatchers.IO) {
        val url = config.baseUrl.trimEnd('/') + "/api/health"
        try {
            client.newCall(Request.Builder().url(url).build()).execute().use { it.isSuccessful }
        } catch (e: IOException) {
            Timber.w(e, "bridge health 检查失败: $url")
            false
        }
    }

    /** 发送 POST 请求，失败按重试策略重试（指数退避） */
    private suspend fun post(path: String, jsonBody: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val url = config.baseUrl.trimEnd('/') + path
            var lastError: Throwable? = null
            for (attempt in 1..config.retry) {
                val builder = Request.Builder().url(url).post(jsonBody.toRequestBody(jsonMedia))
                if (config.token.isNotBlank()) {
                    builder.addHeader("Authorization", "Bearer ${config.token}")
                }
                try {
                    client.newCall(builder.build()).execute().use { resp ->
                        if (resp.isSuccessful) {
                            Timber.i("bridge 请求成功: $path")
                            return@withContext Result.success(Unit)
                        }
                        Timber.w("bridge $url HTTP ${resp.code}（第 $attempt/${config.retry} 次）")
                    }
                } catch (e: IOException) {
                    lastError = e
                    Timber.w(e, "bridge $url 请求异常（第 $attempt/${config.retry} 次）")
                }
                if (attempt < config.retry) {
                    delay(min(1000L shl (attempt - 1), 8000L))
                }
            }
            Result.failure(lastError ?: IOException("bridge $url 重试 ${config.retry} 次仍失败"))
        }
}
