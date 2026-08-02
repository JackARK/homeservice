package club.saltfish.homeservice.config

import club.saltfish.homeservice.rule.ActionDef
import club.saltfish.homeservice.rule.Rule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConfigParseTest {

    @Test
    fun parsesFullConfigWithRules() {
        val json = """
            {
              "bridge": { "baseUrl": "http://1.2.3.4:9092", "token": "t", "timeoutMs": 5000, "retry": 2 },
              "server": { "port": 9000, "token": "s", "allowLanOnly": false },
              "rules": [
                { "id": "r1", "packageNames": ["com.example"], "titleRegex": ".*门铃.*",
                  "textRegex": ".*", "dedupWindowMs": 30000,
                  "actions": [ { "type": "bridgePlayText", "text": "叮咚" } ] }
              ]
            }
        """.trimIndent()
        val config = ConfigManager.parse(json)
        assertEquals("http://1.2.3.4:9092", config.bridge.baseUrl)
        assertEquals("t", config.bridge.token)
        assertEquals(5000, config.bridge.timeoutMs)
        assertEquals(2, config.bridge.retry)
        assertEquals(9000, config.server.port)
        assertEquals(false, config.server.allowLanOnly)
        assertEquals(1, config.rules.size)
        val rule = config.rules[0]
        assertEquals("r1", rule.id)
        assertEquals(listOf("com.example"), rule.packageNames)
        assertEquals(".*门铃.*", rule.titleRegex)
        assertEquals(30000L, rule.dedupWindowMs)
        assertEquals("bridgePlayText", rule.actions[0].type)
        assertEquals("叮咚", rule.actions[0].text)
    }

    @Test
    fun roundTripPreservesConfig() {
        val original = AppConfig(
            bridge = BridgeConfig(baseUrl = "http://h:1", token = "x", timeoutMs = 7000, retry = 4),
            server = ServerConfig(port = 1234, token = "y", allowLanOnly = true),
            rules = listOf(
                Rule(id = "r", packageNames = listOf("p"), actions = listOf(ActionDef("bridgeWakeup")))
            )
        )
        val json = ConfigManager.toJson(original)
        val parsed = ConfigManager.parse(json)
        assertEquals(original.bridge.baseUrl, parsed.bridge.baseUrl)
        assertEquals(original.server.port, parsed.server.port)
        assertEquals(original.rules.size, parsed.rules.size)
        assertEquals(original.rules[0].id, parsed.rules[0].id)
        assertEquals(original.rules[0].actions[0].type, parsed.rules[0].actions[0].type)
    }

    @Test
    fun malformedJsonFallsBackToDefault() {
        val config = ConfigManager.parse("{ 这不是合法 json")
        assertEquals("http://192.168.5.50:9092", config.bridge.baseUrl)
        assertEquals(8888, config.server.port)
        assertTrue(config.rules.isEmpty())
    }

    @Test
    fun defaultValuesWhenFieldsMissing() {
        val config = ConfigManager.parse("{}")
        assertEquals("http://192.168.5.50:9092", config.bridge.baseUrl)
        assertEquals(10_000, config.bridge.timeoutMs)
        assertEquals(3, config.bridge.retry)
        assertEquals(8888, config.server.port)
        // ha 字段缺失时必须回退到默认对象（否则 App 里 OkHttpHomeAssistantClient(config.ha) 会 NPE）
        assertEquals("http://192.168.5.50:8123", config.ha.baseUrl)
        assertEquals(10_000, config.ha.timeoutMs)
        assertTrue(config.rules.isEmpty())
    }
}
