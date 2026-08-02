package club.saltfish.homeservice.ha

import club.saltfish.homeservice.config.HomeAssistantConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
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
 * 基于 OkHttp 的 [HomeAssistantClient] 实现。
 *
 * - 超时：[HomeAssistantConfig.timeoutMs]（连接/读/写统一）
 * - 重试：[HomeAssistantConfig.retry] 次，指数退避（1s → 2s → 4s，上限 8s）
 * - 鉴权：[HomeAssistantConfig.token] 作为 `Authorization: Bearer <token>`
 */
class OkHttpHomeAssistantClient(private val config: HomeAssistantConfig) : HomeAssistantClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .build()

    private val jsonMedia = "application/json; charset=utf-8".toMediaType()
    private val gson = Gson()

    override suspend fun turnOn(entityId: String): Result<Unit> =
        callService(entityId.substringBefore('.'), "turn_on", mapOf("entity_id" to entityId))

    override suspend fun turnOff(entityId: String): Result<Unit> =
        callService(entityId.substringBefore('.'), "turn_off", mapOf("entity_id" to entityId))

    override suspend fun toggle(entityId: String): Result<Unit> =
        callService(entityId.substringBefore('.'), "toggle", mapOf("entity_id" to entityId))

    override suspend fun callService(
        domain: String,
        service: String,
        serviceData: Map<String, Any?>
    ): Result<Unit> = post("/api/services/$domain/$service", gson.toJson(serviceData))

    override suspend fun getState(entityId: String): Result<String> =
        withContext(Dispatchers.IO) {
            val path = "/api/states/$entityId"
            val url = config.baseUrl.trimEnd('/') + path
            var lastError: Throwable? = null
            for (attempt in 1..config.retry) {
                val req = authedBuilder(url).build()
                try {
                    client.newCall(req).execute().use { resp ->
                        if (resp.isSuccessful) {
                            val body = resp.body?.string().orEmpty()
                            val obj = gson.fromJson(body, JsonObject::class.java)
                            val state = obj?.get("state")?.asString
                            if (state != null) {
                                Timber.i("ha getState 成功: $entityId=$state")
                                return@withContext Result.success(state)
                            }
                            Timber.w("ha getState 响应无 state 字段: $path")
                        } else {
                            Timber.w("ha $path HTTP ${resp.code}（第 $attempt/${config.retry} 次）")
                        }
                    }
                } catch (e: IOException) {
                    lastError = e
                    Timber.w(e, "ha $path 请求异常（第 $attempt/${config.retry} 次）")
                }
                if (attempt < config.retry) {
                    delay(min(1000L shl (attempt - 1), 8000L))
                }
            }
            Result.failure(lastError ?: IOException("ha $path 重试 ${config.retry} 次仍失败"))
        }

    override suspend fun health(): Boolean = withContext(Dispatchers.IO) {
        val url = config.baseUrl.trimEnd('/') + "/api/"  // 注意结尾斜杠，/api 不是 /api/
        try {
            client.newCall(authedBuilder(url).build()).execute().use { it.isSuccessful }
        } catch (e: IOException) {
            Timber.w(e, "ha health 检查失败: $url")
            false
        }
    }

    /** 构造带 Bearer 鉴权头的请求 Builder */
    private fun authedBuilder(url: String): Request.Builder =
        Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${config.token}")

    /** 发送 POST 请求，失败按重试策略重试（指数退避） */
    private suspend fun post(path: String, jsonBody: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val url = config.baseUrl.trimEnd('/') + path
            var lastError: Throwable? = null
            for (attempt in 1..config.retry) {
                val builder = authedBuilder(url)
                    .post(jsonBody.toRequestBody(jsonMedia))
                try {
                    client.newCall(builder.build()).execute().use { resp ->
                        if (resp.isSuccessful) {
                            Timber.i("ha 请求成功: $path")
                            return@withContext Result.success(Unit)
                        }
                        Timber.w("ha $url HTTP ${resp.code}（第 $attempt/${config.retry} 次）")
                    }
                } catch (e: IOException) {
                    lastError = e
                    Timber.w(e, "ha $url 请求异常（第 $attempt/${config.retry} 次）")
                }
                if (attempt < config.retry) {
                    delay(min(1000L shl (attempt - 1), 8000L))
                }
            }
            Result.failure(lastError ?: IOException("ha $url 重试 ${config.retry} 次仍失败"))
        }
}
