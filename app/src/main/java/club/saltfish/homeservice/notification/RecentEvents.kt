package club.saltfish.homeservice.notification

/** 一条通知事件（看板展示用）：通知摘要 + 命中的规则 id 列表（空表示未命中） */
data class NotificationEvent(
    val timeMillis: Long,
    val packageName: String,
    val title: String?,
    val text: String?,
    val matchedRuleIds: List<String>
)

/**
 * 最近通知事件环形缓冲（全局单例，容量 50 条）。
 * [NotificationListener] 每收到一条通知写入一条事件，供 /api/status 的看板展示。
 */
object RecentEvents {

    private const val CAPACITY = 50
    private val events = ArrayDeque<NotificationEvent>()

    @Synchronized
    fun add(event: NotificationEvent) {
        events.addLast(event)
        while (events.size > CAPACITY) events.removeFirst()
    }

    /** 最新在前 */
    @Synchronized
    fun list(): List<NotificationEvent> = events.toList().asReversed()
}
