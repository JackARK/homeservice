package club.saltfish.homeservice.log

/** 一条内存日志。[level] 为 android.util.Log 的优先级常量（VERBOSE/DEBUG/INFO/WARN/ERROR） */
data class LogEntry(
    val id: Long,
    val timeMillis: Long,
    val level: Int,
    val tag: String?,
    val message: String
)

/**
 * 内存环形日志缓冲（纯 Kotlin，不依赖 Android 框架，可独立单测）。
 *
 * 供 Web 管理端的日志查看页通过 HTTP API 轮询：
 * - 首次加载用 [query]（不带 afterId）取最新 [limit] 条；
 * - 之后带 afterId=lastId 增量拉取，避免重复传输。
 *
 * 容量固定，写满后淘汰最旧条目，长期运行不涨内存。
 */
class RingLogBuffer(private val capacity: Int = DEFAULT_CAPACITY) {

    companion object {
        const val DEFAULT_CAPACITY = 2000
    }

    private val entries = ArrayDeque<LogEntry>()
    private var nextId = 1L

    /** 追加一条日志，返回写入的条目 */
    @Synchronized
    fun add(level: Int, tag: String?, message: String): LogEntry {
        val entry = LogEntry(nextId++, System.currentTimeMillis(), level, tag, message)
        entries.addLast(entry)
        while (entries.size > capacity) entries.removeFirst()
        return entry
    }

    /**
     * 查询日志。
     *
     * @param level   最低级别（含），null 表示不过滤
     * @param keyword 关键字（匹配 message/tag，忽略大小写），null/空表示不过滤
     * @param afterId 只返回 id 大于该值的条目（增量拉取）；null 表示取最新 [limit] 条
     * @param limit   返回条数上限
     * @return 条目列表（按时间升序）到 当前最大 id（前端轮询游标）
     */
    @Synchronized
    fun query(
        level: Int? = null,
        keyword: String? = null,
        afterId: Long? = null,
        limit: Int = 200
    ): Pair<List<LogEntry>, Long> {
        var seq = entries.asSequence()
        if (afterId != null) seq = seq.filter { it.id > afterId }
        if (level != null) seq = seq.filter { it.level >= level }
        if (!keyword.isNullOrBlank()) {
            seq = seq.filter {
                it.message.contains(keyword, ignoreCase = true) ||
                    it.tag?.contains(keyword, ignoreCase = true) == true
            }
        }
        val filtered = seq.toList()
        // 增量模式取最早 limit 条（翻页向前）；全量模式取最新 limit 条
        val result = if (afterId != null) filtered.take(limit) else filtered.takeLast(limit)
        val lastId = entries.lastOrNull()?.id ?: 0L
        return result to lastId
    }
}
