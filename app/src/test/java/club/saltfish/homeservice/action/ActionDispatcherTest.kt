package club.saltfish.homeservice.action

import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.rule.ActionDef
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/** 手写 fake，避免引入 mockito */
private class FakeBridgeClient : BridgeClient {
    val playedTexts = mutableListOf<String>()
    val playedUrls = mutableListOf<String>()
    var wakeupCount = 0
        private set
    var interruptCount = 0
        private set
    var failPlayText = false

    override suspend fun playText(text: String): Result<Unit> {
        if (failPlayText) return Result.failure(IOException("mock 失败"))
        playedTexts.add(text)
        return Result.success(Unit)
    }

    override suspend fun playUrl(url: String): Result<Unit> {
        playedUrls.add(url)
        return Result.success(Unit)
    }

    override suspend fun wakeup(): Result<Unit> {
        wakeupCount++
        return Result.success(Unit)
    }

    override suspend fun interrupt(): Result<Unit> {
        interruptCount++
        return Result.success(Unit)
    }

    override suspend fun health(): Boolean = true
}

private data class CallServiceRecord(
    val domain: String,
    val service: String,
    val serviceData: Map<String, Any?>
)

private class FakeHomeAssistantClient : HomeAssistantClient {
    val turnedOn = mutableListOf<String>()
    val turnedOff = mutableListOf<String>()
    val toggled = mutableListOf<String>()
    val calledServices = mutableListOf<CallServiceRecord>()
    val queriedStates = mutableListOf<String>()
    var failTurnOn = false

    override suspend fun turnOn(entityId: String): Result<Unit> {
        if (failTurnOn) return Result.failure(IOException("mock 失败"))
        turnedOn.add(entityId)
        return Result.success(Unit)
    }

    override suspend fun turnOff(entityId: String): Result<Unit> {
        turnedOff.add(entityId)
        return Result.success(Unit)
    }

    override suspend fun toggle(entityId: String): Result<Unit> {
        toggled.add(entityId)
        return Result.success(Unit)
    }

    override suspend fun callService(
        domain: String,
        service: String,
        serviceData: Map<String, Any?>
    ): Result<Unit> {
        calledServices.add(CallServiceRecord(domain, service, serviceData))
        return Result.success(Unit)
    }

    override suspend fun getState(entityId: String): Result<String> {
        queriedStates.add(entityId)
        return Result.success("on")
    }

    override suspend fun health(): Boolean = true
}

class ActionDispatcherTest {

    @Test
    fun dispatchesPlayText() = runBlocking {
        val bridge = FakeBridgeClient()
        val dispatcher = ActionDispatcher(bridge, FakeHomeAssistantClient())
        dispatcher.dispatch(listOf(ActionDef("bridgePlayText", text = "你好")))
        assertEquals(listOf("你好"), bridge.playedTexts)
    }

    @Test
    fun dispatchesPlayUrl() = runBlocking {
        val bridge = FakeBridgeClient()
        val dispatcher = ActionDispatcher(bridge, FakeHomeAssistantClient())
        dispatcher.dispatch(listOf(ActionDef("bridgePlayUrl", url = "http://a/b.mp3")))
        assertEquals(listOf("http://a/b.mp3"), bridge.playedUrls)
    }

    @Test
    fun dispatchesWakeupAndInterrupt() = runBlocking {
        val bridge = FakeBridgeClient()
        val dispatcher = ActionDispatcher(bridge, FakeHomeAssistantClient())
        dispatcher.dispatch(listOf(ActionDef("bridgeWakeup"), ActionDef("bridgeInterrupt")))
        assertEquals(1, bridge.wakeupCount)
        assertEquals(1, bridge.interruptCount)
    }

    @Test
    fun skipsActionWithMissingText() = runBlocking {
        val bridge = FakeBridgeClient()
        val dispatcher = ActionDispatcher(bridge, FakeHomeAssistantClient())
        dispatcher.dispatch(listOf(ActionDef("bridgePlayText", text = null)))
        assertTrue(bridge.playedTexts.isEmpty())
    }

    @Test
    fun unknownTypeIsSkipped() = runBlocking {
        val bridge = FakeBridgeClient()
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(bridge, ha)
        dispatcher.dispatch(listOf(ActionDef("doesNotExist")))
        assertTrue(bridge.playedTexts.isEmpty())
        assertTrue(ha.turnedOn.isEmpty())
    }

    @Test
    fun failureOfOneActionDoesNotStopOthers() = runBlocking {
        val bridge = FakeBridgeClient().apply { failPlayText = true }
        val dispatcher = ActionDispatcher(bridge, FakeHomeAssistantClient())
        dispatcher.dispatch(
            listOf(
                ActionDef("bridgePlayText", text = "失败的那个"),
                ActionDef("bridgeInterrupt")
            )
        )
        // 第一个失败，但第二个仍执行
        assertEquals(1, bridge.interruptCount)
    }

    @Test
    fun dispatchesHaTurnOn() = runBlocking {
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(FakeBridgeClient(), ha)
        dispatcher.dispatch(listOf(ActionDef("haTurnOn", entityId = "light.keting")))
        assertEquals(listOf("light.keting"), ha.turnedOn)
    }

    @Test
    fun dispatchesHaTurnOffAndToggle() = runBlocking {
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(FakeBridgeClient(), ha)
        dispatcher.dispatch(
            listOf(
                ActionDef("haTurnOff", entityId = "switch.fan"),
                ActionDef("haToggle", entityId = "light.desk")
            )
        )
        assertEquals(listOf("switch.fan"), ha.turnedOff)
        assertEquals(listOf("light.desk"), ha.toggled)
    }

    @Test
    fun dispatchesHaCallServiceMergesEntityIdAndData() = runBlocking {
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(FakeBridgeClient(), ha)
        dispatcher.dispatch(
            listOf(
                ActionDef(
                    type = "haCallService",
                    domain = "xiaomi_miot",
                    service = "set_property",
                    entityId = "light.x",
                    data = mapOf("field" to "on", "value" to true)
                )
            )
        )
        assertEquals(1, ha.calledServices.size)
        val rec = ha.calledServices.single()
        assertEquals("xiaomi_miot", rec.domain)
        assertEquals("set_property", rec.service)
        assertEquals(
            mapOf("entity_id" to "light.x", "field" to "on", "value" to true),
            rec.serviceData
        )
    }

    @Test
    fun skipsHaActionWithMissingEntityId() = runBlocking {
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(FakeBridgeClient(), ha)
        dispatcher.dispatch(listOf(ActionDef("haTurnOn")))
        assertTrue(ha.turnedOn.isEmpty())
    }

    @Test
    fun skipsHaCallServiceWithMissingDomain() = runBlocking {
        val ha = FakeHomeAssistantClient()
        val dispatcher = ActionDispatcher(FakeBridgeClient(), ha)
        dispatcher.dispatch(
            listOf(ActionDef("haCallService", service = "turn_on", entityId = "light.x"))
        )
        assertTrue(ha.calledServices.isEmpty())
    }

    @Test
    fun failureOfHaActionDoesNotStopBridgeAction() = runBlocking {
        val ha = FakeHomeAssistantClient().apply { failTurnOn = true }
        val bridge = FakeBridgeClient()
        val dispatcher = ActionDispatcher(bridge, ha)
        dispatcher.dispatch(
            listOf(
                ActionDef("haTurnOn", entityId = "light.x"),
                ActionDef("bridgeInterrupt")
            )
        )
        // HA 动作失败，bridge 动作仍执行
        assertEquals(1, bridge.interruptCount)
    }
}
