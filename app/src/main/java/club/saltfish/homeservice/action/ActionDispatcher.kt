package club.saltfish.homeservice.action

import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.rule.ActionDef
import timber.log.Timber

/**
 * 动作分发器。接收规则引擎输出的动作列表，按 [ActionDef.type] 分发执行。
 *
 * 顺序执行（避免多个 TTS 播放相互抢占）；单个动作失败不影响后续动作。
 * welcomeHome 通过函数注入（[welcomeHome] 回调），便于测试替换、避免对具体编排类的依赖。
 */
class ActionDispatcher(
    private val bridge: BridgeClient,
    private val ha: HomeAssistantClient,
    private val welcomeHome: suspend (ActionContext?) -> Result<Unit>
) {

    /** 顺序执行动作列表。[context] 携带触发上下文（开门时间、通知摘要），供 welcomeHome 等使用 */
    suspend fun dispatch(actions: List<ActionDef>, context: ActionContext? = null) {
        for (action in actions) {
            executeOne(action, context)
        }
    }

    private suspend fun executeOne(action: ActionDef, context: ActionContext?) {
        val result: Result<Unit> = when (action.type) {
            "bridgePlayText" -> {
                val text = action.text
                if (text.isNullOrBlank()) {
                    Timber.w("动作 ${action.type} 缺少 text 参数，跳过")
                    return
                }
                bridge.playText(text)
            }
            "bridgePlayUrl" -> {
                val url = action.url
                if (url.isNullOrBlank()) {
                    Timber.w("动作 ${action.type} 缺少 url 参数，跳过")
                    return
                }
                bridge.playUrl(url)
            }
            "bridgeWakeup" -> bridge.wakeup()
            "bridgeInterrupt" -> bridge.interrupt()
            "haTurnOn" -> {
                val id = action.entityId
                if (id.isNullOrBlank()) {
                    Timber.w("动作 ${action.type} 缺少 entityId 参数，跳过")
                    return
                }
                ha.turnOn(id)
            }
            "haTurnOff" -> {
                val id = action.entityId
                if (id.isNullOrBlank()) {
                    Timber.w("动作 ${action.type} 缺少 entityId 参数，跳过")
                    return
                }
                ha.turnOff(id)
            }
            "haToggle" -> {
                val id = action.entityId
                if (id.isNullOrBlank()) {
                    Timber.w("动作 ${action.type} 缺少 entityId 参数，跳过")
                    return
                }
                ha.toggle(id)
            }
            "haCallService" -> {
                val domain = action.domain
                val service = action.service
                if (domain.isNullOrBlank() || service.isNullOrBlank()) {
                    Timber.w("动作 haCallService 缺少 domain/service 参数，跳过")
                    return
                }
                val serviceData = buildMap<String, Any?> {
                    action.entityId?.let { put("entity_id", it) }
                    action.data?.let { putAll(it) }
                }
                ha.callService(domain, service, serviceData)
            }
            "welcomeHome" -> welcomeHome(context)
            else -> {
                Timber.w("未知动作类型：${action.type}，跳过")
                return
            }
        }
        result.onSuccess { Timber.i("动作 ${action.type} 执行成功") }
            .onFailure { Timber.w(it, "动作 ${action.type} 执行失败") }
    }
}
