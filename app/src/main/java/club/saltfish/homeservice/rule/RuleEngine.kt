package club.saltfish.homeservice.rule

import club.saltfish.homeservice.notification.ParsedNotification
import java.util.regex.Pattern

/**
 * 规则匹配引擎（纯逻辑，不依赖 Android 框架，可独立单测）。
 *
 * 对每条规则依次检查：包名 → 标题正则 → 正文正则，全部通过则命中；
 * 命中后经过 [Dedup] 去重，将其动作加入返回列表。
 *
 * @param errorHandler 无效正则等错误回调（默认空实现；上层注入打日志，避免吞异常）
 */
class RuleEngine(
    private val dedup: Dedup = Dedup(),
    private val errorHandler: (ruleId: String, error: Throwable) -> Unit = { _, _ -> }
) {

    /** 用 [rules] 匹配 [notification]，返回所有命中且未去重的动作。 */
    fun match(notification: ParsedNotification, rules: List<Rule>): List<ActionDef> {
        val actions = mutableListOf<ActionDef>()
        for (rule in rules) {
            if (!matchesPackage(rule, notification.packageName)) continue
            if (!matchesRegex(rule.titleRegex, notification.title, rule.id)) continue
            if (!matchesRegex(rule.textRegex, notification.text, rule.id)) continue

            val key = dedupKey(rule.id, notification)
            if (!dedup.shouldProcess(key, rule.dedupWindowMs)) continue

            actions.addAll(rule.actions)
        }
        return actions
    }

    /** 重置去重缓存（如规则热更新后调用） */
    fun resetDedup() = dedup.clear()

    private fun matchesPackage(rule: Rule, packageName: String): Boolean {
        if (rule.packageNames.isEmpty()) return true
        return packageName in rule.packageNames
    }

    private fun matchesRegex(regex: String?, value: String, ruleId: String): Boolean {
        if (regex.isNullOrBlank()) return true
        val pattern = try {
            Pattern.compile(regex)
        } catch (e: Exception) {
            // 无效正则：记录错误并跳过该规则，不影响其它规则匹配
            errorHandler(ruleId, e)
            return false
        }
        return pattern.matcher(value).find()
    }

    private fun dedupKey(ruleId: String, n: ParsedNotification): String =
        "$ruleId|${n.packageName}|${n.title}|${n.text}"
}
