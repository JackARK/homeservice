package club.saltfish.homeservice.config

import org.junit.Assert.assertEquals
import org.junit.Test

class RedactionTest {

    private val config = AppConfig(
        bridge = BridgeConfig(token = "bridge-token"),
        ha = HomeAssistantConfig(token = "ha-token"),
        llm = LlmConfig(apiKey = "sk-xxx"),
        server = ServerConfig(token = "server-token")
    )

    @Test
    fun `redact 将非空敏感字段替换为掩码`() {
        val redacted = Redaction.redact(config)
        assertEquals(Redaction.MASK, redacted.bridge.token)
        assertEquals(Redaction.MASK, redacted.ha.token)
        assertEquals(Redaction.MASK, redacted.llm.apiKey)
        assertEquals(Redaction.MASK, redacted.server.token)
        // 非敏感字段原样保留
        assertEquals(config.bridge.baseUrl, redacted.bridge.baseUrl)
        assertEquals(config.llm.model, redacted.llm.model)
    }

    @Test
    fun `redact 对空敏感字段保持空值不掩码`() {
        val redacted = Redaction.redact(AppConfig())
        assertEquals("", redacted.bridge.token)
        assertEquals("", redacted.llm.apiKey)
    }

    @Test
    fun `mergeSecrets 掩码字段还原为旧值`() {
        val submitted = Redaction.redact(config).copy(bridge = config.bridge.copy(baseUrl = "http://new:1"))
        val merged = Redaction.mergeSecrets(submitted, config)
        assertEquals("bridge-token", merged.bridge.token)
        assertEquals("ha-token", merged.ha.token)
        assertEquals("sk-xxx", merged.llm.apiKey)
        assertEquals("server-token", merged.server.token)
        // 非敏感字段以新值为准
        assertEquals("http://new:1", merged.bridge.baseUrl)
    }

    @Test
    fun `mergeSecrets 用户真实修改敏感字段时以新值为准`() {
        val submitted = Redaction.redact(config).copy(
            ha = config.ha.copy(token = "new-ha-token")
        )
        val merged = Redaction.mergeSecrets(submitted, config)
        assertEquals("new-ha-token", merged.ha.token)
        assertEquals("bridge-token", merged.bridge.token)
    }
}
