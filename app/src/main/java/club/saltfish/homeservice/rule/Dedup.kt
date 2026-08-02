package club.saltfish.homeservice.rule

/**
 * 去重缓存（纯逻辑）。记录每个 key 最近一次触发时间，
 * 在时间窗口内重复出现的 key 被视为重复。
 *
 * @param clock 时间源，默认系统时间；测试可注入虚拟时钟
 */
class Dedup(private val clock: () -> Long = { System.currentTimeMillis() }) {

    private val lastTrigger = mutableMapOf<String, Long>()

    /**
     * 判断 [key] 是否应当处理。
     * @return true 表示窗口内首次出现（应处理）；false 表示窗口内重复（应丢弃）
     */
    fun shouldProcess(key: String, windowMs: Long): Boolean {
        val now = clock()
        val last = lastTrigger[key]
        if (last != null && now - last < windowMs) {
            return false
        }
        lastTrigger[key] = now
        return true
    }

    /** 清空所有去重记录 */
    fun clear() {
        lastTrigger.clear()
    }
}
