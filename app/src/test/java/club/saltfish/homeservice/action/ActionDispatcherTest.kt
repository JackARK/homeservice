package club.saltfish.homeservice.action

import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.rule.ActionDef
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
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

    override suspend fun listVoices(): Result<Map<String, String>> = Result.success(emptyMap())
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

    override suspend fun getState(entityId: String): Result<String> = Result.success("off")
    override suspend fun health(): Boolean = true
}

/** welcomeHome 回调记录器 */
private class WelcomeRecorder {
    var calls = 0
    var lastCtx: ActionContext? = null
    val handler: suspend (ActionContext?) -> Result<Unit> = { ctx ->
        calls++; lastCtx = ctx; Result.success(Unit)
    }
}

/** 把 dispatcher 与其 fake 依赖打包，供用例访问 */
private data class Fixture(
    val dispatcher: ActionDispatcher,
    val bridge: FakeBridgeClient,
    val ha: FakeHomeAssistantClient,
    val welcome: WelcomeRecorder
)

private fun fixture(
    bridge: FakeBridgeClient = FakeBridgeClient(),
    ha: FakeHomeAssistantClient = FakeHomeAssistantClient(),
    welcome: WelcomeRecorder = WelcomeRecorder()
) = Fixture(ActionDispatcher(bridge, ha, welcome.handler), bridge, ha, welcome)

class ActionDispatcherTest {

    @Test
    fun dispatchesPlayText() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("bridgePlayText", text = "你好")))
        assertEquals(listOf("你好"), f.bridge.playedTexts)
    }

    @Test
    fun dispatchesPlayUrl() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("bridgePlayUrl", url = "http://a/b.mp3")))
        assertEquals(listOf("http://a/b.mp3"), f.bridge.playedUrls)
    }

    @Test
    fun dispatchesWakeupAndInterrupt() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("bridgeWakeup"), ActionDef("bridgeInterrupt")))
        assertEquals(1, f.bridge.wakeupCount)
        assertEquals(1, f.bridge.interruptCount)
    }

    @Test
    fun skipsActionWithMissingText() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("bridgePlayText", text = null)))
        assertTrue(f.bridge.playedTexts.isEmpty())
    }

    @Test
    fun unknownTypeIsSkipped() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("doesNotExist")))
        assertTrue(f.bridge.playedTexts.isEmpty())
        assertTrue(f.ha.turnedOn.isEmpty())
    }

    @Test
    fun failureOfOneActionDoesNotStopOthers() = runBlocking {
        val f = fixture(bridge = FakeBridgeClient().apply { failPlayText = true })
        f.dispatcher.dispatch(
            listOf(
                ActionDef("bridgePlayText", text = "失败的那个"),
                ActionDef("bridgeInterrupt")
            )
        )
        assertEquals(1, f.bridge.interruptCount)
    }

    @Test
    fun dispatchesHaTurnOn() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("haTurnOn", entityId = "light.keting")))
        assertEquals(listOf("light.keting"), f.ha.turnedOn)
    }

    @Test
    fun dispatchesHaTurnOffAndToggle() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(
            listOf(
                ActionDef("haTurnOff", entityId = "switch.fan"),
                ActionDef("haToggle", entityId = "light.desk")
            )
        )
        assertEquals(listOf("switch.fan"), f.ha.turnedOff)
        assertEquals(listOf("light.desk"), f.ha.toggled)
    }

    @Test
    fun dispatchesHaCallServiceMergesEntityIdAndData() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(
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
        assertEquals(1, f.ha.calledServices.size)
        val rec = f.ha.calledServices.single()
        assertEquals("xiaomi_miot", rec.domain)
        assertEquals("set_property", rec.service)
        assertEquals(
            mapOf("entity_id" to "light.x", "field" to "on", "value" to true),
            rec.serviceData
        )
    }

    @Test
    fun skipsHaActionWithMissingEntityId() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("haTurnOn")))
        assertTrue(f.ha.turnedOn.isEmpty())
    }

    @Test
    fun skipsHaCallServiceWithMissingDomain() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(
            listOf(ActionDef("haCallService", service = "turn_on", entityId = "light.x"))
        )
        assertTrue(f.ha.calledServices.isEmpty())
    }

    @Test
    fun failureOfHaActionDoesNotStopBridgeAction() = runBlocking {
        val f = fixture(bridge = FakeBridgeClient(), ha = FakeHomeAssistantClient().apply { failTurnOn = true })
        f.dispatcher.dispatch(
            listOf(
                ActionDef("haTurnOn", entityId = "light.x"),
                ActionDef("bridgeInterrupt")
            )
        )
        assertEquals(1, f.bridge.interruptCount)
    }

    @Test
    fun welcomeHomeForwardsContext() = runBlocking {
        val f = fixture()
        val ctx = ActionContext(triggerTimeMs = 12345L, notificationSummary = "门铃 有人按门铃")
        f.dispatcher.dispatch(listOf(ActionDef("welcomeHome")), ctx)
        assertEquals(1, f.welcome.calls)
        assertSame(ctx, f.welcome.lastCtx)
    }

    @Test
    fun welcomeHomeWithNullContext() = runBlocking {
        val f = fixture()
        f.dispatcher.dispatch(listOf(ActionDef("welcomeHome")))
        assertEquals(1, f.welcome.calls)
        assertNull(f.welcome.lastCtx)
    }
}
