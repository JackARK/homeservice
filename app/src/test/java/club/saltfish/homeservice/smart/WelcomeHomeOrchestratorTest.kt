package club.saltfish.homeservice.smart

import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.config.SmartHomeConfig
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.llm.LlmClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/** 可控 HA：states 提供 getState 返回值；记录 callService/turnOn 调用 */
private class FakeHA : HomeAssistantClient {
    val states = mutableMapOf<String, String>()
    val called = mutableListOf<Triple<String, String, Map<String, Any?>>>()
    val turnedOn = mutableListOf<String>()
    override suspend fun turnOn(entityId: String): Result<Unit> {
        turnedOn.add(entityId); return Result.success(Unit)
    }
    override suspend fun turnOff(entityId: String): Result<Unit> = Result.success(Unit)
    override suspend fun toggle(entityId: String): Result<Unit> = Result.success(Unit)
    override suspend fun callService(domain: String, service: String, serviceData: Map<String, Any?>): Result<Unit> {
        called.add(Triple(domain, service, serviceData)); return Result.success(Unit)
    }
    override suspend fun getState(entityId: String): Result<String> =
        states[entityId]?.let { Result.success(it) } ?: Result.failure(IOException("no state"))
    override suspend fun health(): Boolean = true
}

private class FakeBridge : BridgeClient {
    val played = mutableListOf<String>()
    override suspend fun playText(text: String): Result<Unit> {
        played.add(text); return Result.success(Unit)
    }
    override suspend fun playUrl(url: String): Result<Unit> = Result.success(Unit)
    override suspend fun wakeup(): Result<Unit> = Result.success(Unit)
    override suspend fun interrupt(): Result<Unit> = Result.success(Unit)
    override suspend fun health(): Boolean = true
}

private class FakeLlm : LlmClient {
    var lastSystem: String? = null
    var lastUser: String? = null
    var reply = "欢迎回家"
    var fail = false
    override suspend fun chat(systemPrompt: String, userPrompt: String): Result<String> {
        lastSystem = systemPrompt; lastUser = userPrompt
        return if (fail) Result.failure(IOException("llm mock fail")) else Result.success(reply)
    }
}

class WelcomeHomeOrchestratorTest {

    private val fixedTime = 1754237700000L // 固定时刻，避免依赖当前时间

    private fun newOrchestrator(
        ha: FakeHA, bridge: FakeBridge, llm: FakeLlm, config: SmartHomeConfig = SmartHomeConfig()
    ) = WelcomeHomeOrchestrator(ha, bridge, llm, config) { fixedTime }

    /** temp=null 表示温度实体读不到（getState 失败） */
    private fun haWith(temp: String?, sun: String, ac: String, light: String) = FakeHA().apply {
        temp?.let { states["sensor.xiaomi_cn_2112890261_w2_temperature_p_2_7"] = it }
        states["sun.sun"] = sun
        states["climate.04c9de47c85e_climate"] = ac
        states["switch.xiaomi_cn_2112890261_w2_on_p_3_1"] = light
    }

    @Test
    fun hotAndDark_opensAcAndLight() = runBlocking {
        val ha = haWith("28", "below_horizon", "off", "off")
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        // 开空调：climate.set_temperature
        assertEquals(1, ha.called.size)
        val (domain, service, data) = ha.called.first()
        assertEquals("climate", domain)
        assertEquals("set_temperature", service)
        assertEquals("cool", data["hvac_mode"])
        assertEquals(26.0, data["temperature"])
        // 开灯
        assertEquals(listOf("switch.xiaomi_cn_2112890261_w2_on_p_3_1"), ha.turnedOn)
        // LLM 被调用，播报其回复
        assertNotNull(llm.lastUser)
        assertTrue(llm.lastUser!!.contains("天黑"))
        assertEquals("欢迎回家", bridge.played.first())
    }

    @Test
    fun coolAndDark_onlyLight() = runBlocking {
        val ha = haWith("24", "below_horizon", "off", "off")
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        assertTrue("不应开空调", ha.called.isEmpty())
        assertEquals(1, ha.turnedOn.size)
    }

    @Test
    fun hotAndDaytime_onlyAc() = runBlocking {
        val ha = haWith("28", "above_horizon", "off", "off")
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        assertEquals(1, ha.called.size)
        assertTrue("白天不应开灯", ha.turnedOn.isEmpty())
    }

    @Test
    fun temperatureUnavailable_skipsAc() = runBlocking {
        val ha = haWith(null, "below_horizon", "off", "off")
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        assertTrue("温度读不到应保守不开空调", ha.called.isEmpty())
        assertEquals(1, ha.turnedOn.size)
    }

    @Test
    fun acAlreadyRunning_skipsAc() = runBlocking {
        val ha = haWith("30", "below_horizon", "cool", "off")
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        assertTrue("空调在运行不应重复开", ha.called.isEmpty())
        assertEquals(1, ha.turnedOn.size)
        assertTrue(llm.lastUser!!.contains("空调本来就在运行"))
    }

    @Test
    fun llmFailure_usesFallback() = runBlocking {
        val ha = haWith("28", "below_horizon", "off", "off")
        val bridge = FakeBridge(); val llm = FakeLlm().apply { fail = true }
        newOrchestrator(ha, bridge, llm).welcomeHome(null)
        assertEquals(listOf("欢迎回家"), bridge.played)
    }

    @Test
    fun weatherIncludedInPrompt_whenConfigured() = runBlocking {
        val cfg = SmartHomeConfig(weatherEntity = "weather.home")
        val ha = haWith("28", "below_horizon", "off", "off").apply {
            states["weather.home"] = "rainy"
        }
        val bridge = FakeBridge(); val llm = FakeLlm()
        newOrchestrator(ha, bridge, llm, cfg).welcomeHome(null)
        assertTrue("prompt 应含天气中文描述", llm.lastUser!!.contains("下雨"))
    }
}
