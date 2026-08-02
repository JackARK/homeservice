package club.saltfish.homeservice.notification

/** 规范化后的通知数据，供规则引擎匹配使用 */
data class ParsedNotification(
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long
)
