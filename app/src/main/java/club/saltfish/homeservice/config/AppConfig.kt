package club.saltfish.homeservice.config

import club.saltfish.homeservice.rule.Rule

/** open-xiaoai-bridge 连接配置 */
data class BridgeConfig(
    val baseUrl: String = "http://192.168.5.50:9092",
    val token: String = "",
    val timeoutMs: Int = 10_000,
    val retry: Int = 3
)

/** 内嵌 HTTP 服务器配置 */
data class ServerConfig(
    val port: Int = 8888,
    val token: String = "",
    val allowLanOnly: Boolean = true
)

/** Home Assistant 连接配置（控制接入 HA 的米家等设备） */
data class HomeAssistantConfig(
    val baseUrl: String = "http://192.168.5.50:8123",
    val token: String = "",
    val timeoutMs: Int = 10_000,
    val retry: Int = 3
)

/** LLM 配置（DeepSeek，OpenAI 兼容）。apiKey 运行时填，不进仓库 */
data class LlmConfig(
    val baseUrl: String = "https://api.deepseek.com",
    val apiKey: String = "",
    val model: String = "deepseek-v4-flash",
    val timeoutMs: Int = 15_000,
    val maxTokens: Int = 100
)

/** 智能回家场景配置（welcomeHome 动作） */
data class SmartHomeConfig(
    val temperatureSensor: String = "sensor.xiaomi_cn_2112890261_w2_temperature_p_2_7",
    val sunEntity: String = "sun.sun",
    val acEntityId: String = "climate.04c9de47c85e_climate",
    val lightEntityId: String = "switch.xiaomi_cn_2112890261_w2_on_p_3_1",
    val temperatureThreshold: Double = 26.0,
    val acTargetTemp: Double = 26.0,
    val acHvacMode: String = "cool",
    /** 天气实体（如 weather.home），留空则不采集天气。配好 HA 天气集成后填入 */
    val weatherEntity: String? = null
)

/** 应用总配置（对应 config.json） */
data class AppConfig(
    val bridge: BridgeConfig = BridgeConfig(),
    val server: ServerConfig = ServerConfig(),
    val ha: HomeAssistantConfig = HomeAssistantConfig(),
    val llm: LlmConfig = LlmConfig(),
    val smartHome: SmartHomeConfig = SmartHomeConfig(),
    val rules: List<Rule> = emptyList()
)
