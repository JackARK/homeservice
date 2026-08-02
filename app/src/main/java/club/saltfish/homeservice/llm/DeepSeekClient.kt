package club.saltfish.homeservice.llm

import club.saltfish.homeservice.config.LlmConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 基于 OkHttp 的 DeepSeek（OpenAI 兼容）[LlmClient] 实现。
 *
 * - 端点：POST {baseUrl}/chat/completions
 * - 鉴权：Authorization: Bearer {apiKey}
 * - 默认非流式、关闭思考（thinking disabled），追求快速回复
 * - 不重试：LLM 响应慢，失败由调用方降级
 */
class DeepSeekClient(private val config: LlmConfig) : LlmClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(config.timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        .build()

    private val jsonMedia = "application/json; charset=utf-8".toMediaType()
    private val gson = Gson()

    override suspend fun chat(systemPrompt: String, userPrompt: String): Result<String> =
        withContext(Dispatchers.IO) {
            val body = gson.toJson(
                mapOf(
                    "model" to config.model,
                    "messages" to listOf(
                        mapOf("role" to "system", "content" to systemPrompt),
                        mapOf("role" to "user", "content" to userPrompt)
                    ),
                    "stream" to false,
                    "max_tokens" to config.maxTokens,
                    "thinking" to mapOf("type" to "disabled") // 关闭思考，快速回复
                )
            )
            val req = Request.Builder()
                .url(config.baseUrl.trimEnd('/') + "/chat/completions")
                .addHeader("Authorization", "Bearer ${config.apiKey}")
                .post(body.toRequestBody(jsonMedia))
                .build()
            try {
                client.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        val errBody = resp.body?.string()?.take(200).orEmpty()
                        Timber.w("llm HTTP ${resp.code}: $errBody")
                        return@withContext Result.failure(IOException("llm HTTP ${resp.code}"))
                    }
                    val json = gson.fromJson(resp.body?.string().orEmpty(), JsonObject::class.java)
                    val content = json?.getAsJsonArray("choices")
                        ?.firstOrNull()?.asJsonObject
                        ?.getAsJsonObject("message")
                        ?.get("content")?.asString
                    if (content != null) {
                        Timber.i("llm 生成成功: ${content.take(40)}")
                        Result.success(content.trim())
                    } else {
                        Timber.w("llm 响应无 content")
                        Result.failure(IOException("llm 响应无 content"))
                    }
                }
            } catch (e: IOException) {
                Timber.w(e, "llm 请求异常")
                Result.failure(e)
            }
        }
}
