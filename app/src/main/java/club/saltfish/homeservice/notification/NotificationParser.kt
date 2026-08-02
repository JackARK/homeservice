package club.saltfish.homeservice.notification

/**
 * 通知解析（纯逻辑，不依赖 Android 框架，可独立单测）。
 *
 * 真实的 StatusBarNotification 字段提取由 NotificationListener 完成，
 * 这里只负责字段规范化与优先级兜底。
 */
object NotificationParser {

    /**
     * @param title  通知标题（Notification.Extras.title）
     * @param text   通知正文（Notification.Extras.text）
     * @param ticker ticker 文本（兜底）
     * @param postTime 通知触发时间戳
     */
    fun parse(
        packageName: String,
        title: String?,
        text: String?,
        ticker: String?,
        postTime: Long
    ): ParsedNotification {
        // 标题优先级：显式 title > ticker > 包名兜底
        val resolvedTitle = title?.takeIf { it.isNotBlank() }
            ?: ticker?.takeIf { it.isNotBlank() }
            ?: packageName
        // 正文优先级：text > ticker > 空串
        val resolvedText = text?.takeIf { it.isNotBlank() }
            ?: ticker?.takeIf { it.isNotBlank() }
            ?: ""
        return ParsedNotification(
            packageName = packageName.trim(),
            title = resolvedTitle.trim(),
            text = resolvedText.trim(),
            postTime = postTime
        )
    }
}
