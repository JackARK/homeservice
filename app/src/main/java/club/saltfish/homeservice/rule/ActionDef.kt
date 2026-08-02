package club.saltfish.homeservice.rule

/**
 * 动作定义（来自规则配置）。用 [type] 字段区分类型，便于 Gson 直接序列化，
 * 由 ActionDispatcher 根据 [type] 分发执行。
 *
 * type 取值：
 * - "bridgePlayText"  播放文字（TTS），需提供 [text]
 * - "bridgePlayUrl"   播放音频链接，需提供 [url]
 * - "bridgeWakeup"    唤醒小爱音箱
 * - "bridgeInterrupt" 打断当前播放
 * - "haTurnOn"        打开 HA 实体，需提供 [entityId]
 * - "haTurnOff"       关闭 HA 实体，需提供 [entityId]
 * - "haToggle"        切换 HA 实体，需提供 [entityId]
 * - "haCallService"   调用 HA 任意服务，需提供 [domain]+[service]，可选 [entityId]、[data]
 * - "welcomeHome"     智能回家：按室温/日落动态决定开空调与客厅灯，并用 DeepSeek 生成播报
 */
data class ActionDef(
    val type: String,
    val text: String? = null,
    val url: String? = null,
    val domain: String? = null,
    val service: String? = null,
    val entityId: String? = null,
    val data: Map<String, Any?>? = null
)
