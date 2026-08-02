package club.saltfish.homeservice.rule

import club.saltfish.homeservice.notification.ParsedNotification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleEngineTest {

    private fun notification(pkg: String, title: String, text: String) =
        ParsedNotification(pkg, title, text, 0L)

    @Test
    fun matchesByPackageName() {
        val rule = Rule(
            id = "r1",
            packageNames = listOf("com.example"),
            actions = listOf(ActionDef("bridgePlayText", text = "命中"))
        )
        val engine = RuleEngine()
        val result = engine.match(notification("com.example", "标题", "正文"), listOf(rule))
        assertEquals(1, result.size)
        assertEquals("bridgePlayText", result[0].type)
    }

    @Test
    fun doesNotMatchDifferentPackage() {
        val rule = Rule(id = "r1", packageNames = listOf("com.example"))
        val engine = RuleEngine()
        val result = engine.match(notification("com.other", "标题", "正文"), listOf(rule))
        assertTrue(result.isEmpty())
    }

    @Test
    fun emptyPackageListMatchesAny() {
        val rule = Rule(id = "r1", packageNames = emptyList(), actions = listOf(ActionDef("bridgePlayText")))
        val engine = RuleEngine()
        val result = engine.match(notification("anything", "t", "x"), listOf(rule))
        assertEquals(1, result.size)
    }

    @Test
    fun titleRegexFiltersMatches() {
        val rule = Rule(id = "r1", packageNames = listOf("com.example"), titleRegex = ".*报警.*", actions = listOf(ActionDef("bridgePlayText")))
        val engine = RuleEngine()
        assertTrue(engine.match(notification("com.example", "烟雾报警", ""), listOf(rule)).isNotEmpty())
        assertTrue(engine.match(notification("com.example", "正常", ""), listOf(rule)).isEmpty())
    }

    @Test
    fun dedupSuppressesRepeatWithinWindow() {
        val rule = Rule(
            id = "r1",
            packageNames = listOf("com.example"),
            actions = listOf(ActionDef("bridgePlayText")),
            dedupWindowMs = 1000
        )
        var time = 0L
        val engine = RuleEngine(Dedup { time })
        val n = notification("com.example", "标题", "正文")
        assertEquals(1, engine.match(n, listOf(rule)).size) // 首次命中
        time = 500
        assertTrue(engine.match(n, listOf(rule)).isEmpty()) // 窗口内重复被去重
        time = 1001
        assertEquals(1, engine.match(n, listOf(rule)).size) // 窗口外再次命中
    }

    @Test
    fun invalidRegexTriggersErrorHandlerAndSkipsRule() {
        val rule = Rule(id = "bad", packageNames = listOf("com.example"), titleRegex = "[invalid")
        val errors = mutableListOf<String>()
        val engine = RuleEngine(errorHandler = { id, _ -> errors.add(id) })
        val result = engine.match(notification("com.example", "标题", ""), listOf(rule))
        assertTrue(result.isEmpty())
        assertEquals(listOf("bad"), errors)
    }

    @Test
    fun multipleRulesAccumulateActions() {
        val r1 = Rule(id = "r1", packageNames = listOf("com.example"), actions = listOf(ActionDef("bridgePlayText", text = "A")))
        val r2 = Rule(id = "r2", packageNames = listOf("com.example"), actions = listOf(ActionDef("bridgeInterrupt")))
        val engine = RuleEngine()
        val result = engine.match(notification("com.example", "t", "x"), listOf(r1, r2))
        assertEquals(2, result.size)
    }
}
