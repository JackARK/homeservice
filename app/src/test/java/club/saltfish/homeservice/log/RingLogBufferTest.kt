package club.saltfish.homeservice.log

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RingLogBufferTest {

    @Test
    fun `容量满后淘汰最旧条目`() {
        val buffer = RingLogBuffer(capacity = 3)
        repeat(5) { buffer.add(4, "tag", "msg$it") }
        val (entries, _) = buffer.query()
        assertEquals(3, entries.size)
        assertEquals(listOf("msg2", "msg3", "msg4"), entries.map { it.message })
    }

    @Test
    fun `afterId 增量拉取只返回更新的条目`() {
        val buffer = RingLogBuffer()
        buffer.add(4, "tag", "a")
        val (_, cursor) = buffer.query()
        buffer.add(4, "tag", "b")
        buffer.add(4, "tag", "c")
        val (entries, lastId) = buffer.query(afterId = cursor)
        assertEquals(listOf("b", "c"), entries.map { it.message })
        assertTrue(lastId > cursor)
    }

    @Test
    fun `level 过滤返回不低于指定级别的条目`() {
        val buffer = RingLogBuffer()
        buffer.add(2, "tag", "verbose")
        buffer.add(3, "tag", "debug")
        buffer.add(4, "tag", "info")
        buffer.add(5, "tag", "warn")
        val (entries, _) = buffer.query(level = 4)
        assertEquals(listOf("info", "warn"), entries.map { it.message })
    }

    @Test
    fun `keyword 过滤匹配 message 与 tag 且忽略大小写`() {
        val buffer = RingLogBuffer()
        buffer.add(4, "Bridge", "请求成功")
        buffer.add(4, "other", "收到通知 pkg=com.x")
        val (byMessage, _) = buffer.query(keyword = "PKG=")
        assertEquals(listOf("收到通知 pkg=com.x"), byMessage.map { it.message })
        val (byTag, _) = buffer.query(keyword = "bridge")
        assertEquals(listOf("请求成功"), byTag.map { it.message })
    }

    @Test
    fun `全量查询返回最新 limit 条且按时间升序`() {
        val buffer = RingLogBuffer()
        repeat(10) { buffer.add(4, "tag", "msg$it") }
        val (entries, _) = buffer.query(limit = 3)
        assertEquals(listOf("msg7", "msg8", "msg9"), entries.map { it.message })
    }
}
