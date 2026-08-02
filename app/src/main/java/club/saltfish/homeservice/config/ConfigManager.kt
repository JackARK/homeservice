package club.saltfish.homeservice.config

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.io.File

/**
 * 配置加载与持久化。
 *
 * 配置文件存放在应用内部存储 [Context.getFilesDir]/config.json，
 * 首次启动从 assets/config.default.json 复制默认配置。
 *
 * 解析/序列化逻辑（[parse]/[toJson]）为纯函数，可独立单测；
 * 文件 IO 依赖 Android Context。
 */
object ConfigManager {

    private const val CONFIG_FILE = "config.json"
    private const val DEFAULT_ASSET = "config.default.json"

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    /** 从 JSON 字符串解析配置；解析失败返回默认配置并打错误日志 */
    fun parse(json: String): AppConfig {
        return try {
            gson.fromJson(json, AppConfig::class.java) ?: AppConfig()
        } catch (e: Exception) {
            Timber.e(e, "配置解析失败，使用默认配置")
            AppConfig()
        }
    }

    /** 将配置序列化为 JSON 字符串 */
    fun toJson(config: AppConfig): String = gson.toJson(config)

    /** 加载配置：若内部存储无配置文件，则从 assets 复制默认配置 */
    fun load(context: Context): AppConfig {
        val file = File(context.filesDir, CONFIG_FILE)
        if (!file.exists()) {
            copyDefault(context, file)
        }
        val json = file.readText()
        return parse(json)
    }

    /** 保存配置到内部存储（支持 HTTP API 热更新后持久化） */
    fun save(context: Context, config: AppConfig) {
        val file = File(context.filesDir, CONFIG_FILE)
        file.writeText(toJson(config))
        Timber.i("配置已保存至 ${file.absolutePath}")
    }

    private fun copyDefault(context: Context, target: File) {
        try {
            context.assets.open(DEFAULT_ASSET).use { input ->
                target.outputStream().use { output -> input.copyTo(output) }
            }
            Timber.i("已从 assets 复制默认配置")
        } catch (e: Exception) {
            Timber.e(e, "复制默认配置失败，写入空配置")
            target.writeText(toJson(AppConfig()))
        }
    }
}
