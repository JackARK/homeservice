package club.saltfish.homeservice.notification

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationParserTest {

    @Test
    fun usesExplicitTitleAndTextWhenPresent() {
        val n = NotificationParser.parse("com.example", "标题", "正文", "ticker", 1000L)
        assertEquals("com.example", n.packageName)
        assertEquals("标题", n.title)
        assertEquals("正文", n.text)
        assertEquals(1000L, n.postTime)
    }

    @Test
    fun fallsBackToTickerWhenTitleBlank() {
        val n = NotificationParser.parse("com.example", "  ", null, "ticker文本", 0L)
        assertEquals("ticker文本", n.title)
    }

    @Test
    fun fallsBackToPackageNameWhenTitleAndTickerBlank() {
        val n = NotificationParser.parse("com.example", null, "正文", "", 0L)
        assertEquals("com.example", n.title)
    }

    @Test
    fun textFallsBackToTicker() {
        val n = NotificationParser.parse("com.example", "标题", null, "兜底正文", 0L)
        assertEquals("兜底正文", n.text)
    }

    @Test
    fun textIsEmptyWhenAllBlank() {
        val n = NotificationParser.parse("com.example", "标题", "  ", "  ", 0L)
        assertEquals("", n.text)
    }

    @Test
    fun trimsWhitespace() {
        val n = NotificationParser.parse("  com.example  ", "  标题  ", "  正文  ", null, 0L)
        assertEquals("com.example", n.packageName)
        assertEquals("标题", n.title)
        assertEquals("正文", n.text)
    }
}
