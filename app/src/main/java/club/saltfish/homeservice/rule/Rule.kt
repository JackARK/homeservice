package club.saltfish.homeservice.rule

/**
 * 一条通知匹配规则。
 *
 * @param id            规则唯一标识，用于日志与去重
 * @param packageNames  命中包名集合（空表示匹配任意包名）
 * @param titleRegex    标题正则（null/空表示不限制）
 * @param textRegex     正文正则（null/空表示不限制）
 * @param actions       命中后执行的动作列表
 * @param dedupWindowMs 去重时间窗口（毫秒），窗口内相同通知只处理一次
 */
data class Rule(
    val id: String,
    val packageNames: List<String> = emptyList(),
    val titleRegex: String? = null,
    val textRegex: String? = null,
    val actions: List<ActionDef> = emptyList(),
    val dedupWindowMs: Long = 60_000L
)
