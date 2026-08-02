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

/** 应用总配置（对应 config.json） */
data class AppConfig(
    val bridge: BridgeConfig = BridgeConfig(),
    val server: ServerConfig = ServerConfig(),
    val ha: HomeAssistantConfig = HomeAssistantConfig(),
    val rules: List<Rule> = emptyList()
)
