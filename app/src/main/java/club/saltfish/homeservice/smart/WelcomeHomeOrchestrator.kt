package club.saltfish.homeservice.smart

import club.saltfish.homeservice.action.ActionContext
import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.config.SmartHomeConfig
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.llm.LlmClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 智能回家编排器。
 *
 * 流程：并行查 HA 环境状态（室温/天色/天气/空调/灯）→ 决策开空调与客厅灯
 * → 执行 HA 动作 → DeepSeek 生成欢迎语 → 音箱播报。
 *
 * 降级原则：任一状态读不到则跳过该项决策（温度读不到就保守不开空调）；
 * LLM 失败用兜底语；不阻断整体。
 */
class WelcomeHomeOrchestrator(
    private val ha: HomeAssistantClient,
    private val bridge: BridgeClient,
    private val llm: LlmClient,
    private val config: SmartHomeConfig,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {
    companion object {
        private const val SYSTEM_PROMPT =
            "你是温馨的家庭管家。根据主人回家的环境情况，用一句简短自然、像家人说话的口语欢迎主人。" +
                "不要用书面语，不要分点，不要加引号，30字以内。"
        private const val FALLBACK_TEXT = "欢迎回家"

        /** 空调"正在运行"的 hvac_action/state，避免重复下发 */
        private val AC_RUNNING = setOf("cool", "heat", "auto", "dry", "fan_only", "heat_cool")

        /** HA weather condition（英文）→ 中文 */
        private val CONDITION_CN = mapOf(
            "clear-night" to "夜间晴朗", "sunny" to "晴", "cloudy" to "多云",
            "partlycloudy" to "多云", "rainy" to "下雨", "pouring" to "暴雨",
            "lightning" to "雷电", "lightning-rainy" to "雷雨", "snowy" to "下雪",
            "snowy-rainy" to "雨夹雪", "fog" to "有雾", "windy" to "大风",
            "hail" to "冰雹", "exceptional" to "极端天气"
        )
    }

    /** 执行智能回家流程 */
    suspend fun welcomeHome(context: ActionContext?): Result<Unit> {
        val triggerTimeMs = context?.triggerTimeMs ?: clock()
        val env = fetchEnvironment()

        val turnOnAc = shouldTurnOnAc(env)
        val turnOnLight = shouldTurnOnLight(env)
        Timber.i(
            "welcomeHome 决策: 室温=${env.temperature} 天色=${env.sunState} 天气=${env.weather} " +
                "ac=${env.acState} 灯=${env.lightState} → 开空调=$turnOnAc 开灯=$turnOnLight"
        )

        if (turnOnAc) {
            ha.callService(
                "climate", "set_temperature",
                mapOf(
                    "entity_id" to config.acEntityId,
                    "hvac_mode" to config.acHvacMode,
                    "temperature" to config.acTargetTemp
                )
            ).onFailure { Timber.w(it, "welcomeHome 开空调失败") }
        }
        if (turnOnLight) {
            ha.turnOn(config.lightEntityId).onFailure { Timber.w(it, "welcomeHome 开灯失败") }
        }

        val speech = generateSpeech(env, turnOnAc, turnOnLight, triggerTimeMs, context?.notificationSummary)
        bridge.playText(speech).onFailure { Timber.w(it, "welcomeHome 播报失败") }
        return Result.success(Unit)
    }

    /** 并行拉取环境状态；单项失败返回 null，不阻塞其它 */
    private suspend fun fetchEnvironment(): Environment = coroutineScope {
        val temp = async { ha.getState(config.temperatureSensor).getOrNull()?.toDoubleOrNull() }
        val sun = async { ha.getState(config.sunEntity).getOrNull() }
        val ac = async { ha.getState(config.acEntityId).getOrNull() }
        val light = async { ha.getState(config.lightEntityId).getOrNull() }
        val weather = async { config.weatherEntity?.let { ha.getState(it).getOrNull() } }
        Environment(temp.await(), sun.await(), ac.await(), light.await(), weather.await())
    }

    private fun shouldTurnOnAc(env: Environment): Boolean {
        val t = env.temperature ?: return false // 温度读不到，保守不开
        return t >= config.temperatureThreshold && env.acState !in AC_RUNNING
    }

    private fun shouldTurnOnLight(env: Environment): Boolean =
        env.sunState == "below_horizon" && env.lightState != "on"

    /** 组装上下文并生成；LLM 失败用兜底语 */
    private suspend fun generateSpeech(
        env: Environment, turnedOnAc: Boolean, turnedOnLight: Boolean,
        triggerTimeMs: Long, summary: String?
    ): String {
        val userPrompt = buildUserPrompt(env, turnedOnAc, turnedOnLight, triggerTimeMs, summary)
        Timber.d("welcomeHome LLM userPrompt:\n$userPrompt")
        return llm.chat(SYSTEM_PROMPT, userPrompt).getOrElse {
            Timber.w(it, "welcomeHome LLM 失败，使用兜底语")
            FALLBACK_TEXT
        }
    }

    private fun buildUserPrompt(
        env: Environment, turnedOnAc: Boolean, turnedOnLight: Boolean,
        triggerTimeMs: Long, summary: String?
    ): String = buildString {
        val cal = Calendar.getInstance().apply { timeInMillis = triggerTimeMs }
        val week = arrayOf("日", "一", "二", "三", "四", "五", "六")[cal.get(Calendar.DAY_OF_WEEK) - 1]
        append("今天是").append(SimpleDateFormat("M月d日", Locale.CHINA).format(Date(triggerTimeMs)))
            .append("星期").append(week).append("。\n")
        append("现在是").append(SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(triggerTimeMs)))
            .append("，主人刚到家开门。\n")

        append("天色：").append(when (env.sunState) {
            "below_horizon" -> "太阳已落山，天黑了"
            "above_horizon" -> "天还亮着"
            else -> "天色未知"
        }).append("。\n")

        // 天气（仅当配置了 weatherEntity 且读到时）
        env.weather?.let { append("外面天气：").append(CONDITION_CN[it] ?: it).append("。\n") }

        // 室内温度与体感
        env.temperature?.let {
            append("室内温度").append(it).append("°C")
            append(when {
                it >= 28 -> "，有点热"
                it >= 26 -> "，有点闷"
                else -> "，挺舒适"
            }).append("。\n")
        }

        // 实际操作结果
        append("系统为主人做的：")
        val acts = mutableListOf<String>()
        if (turnedOnAc) {
            acts.add("打开了客厅空调制冷（设定${config.acTargetTemp.toInt()}°C）")
        } else if (env.acState in AC_RUNNING) {
            acts.add("空调本来就在运行")
        }
        if (turnedOnLight) {
            acts.add("打开了客厅灯")
        } else if (env.sunState == "below_horizon") {
            acts.add("客厅灯已是开着的")
        }
        append(if (acts.isEmpty()) "暂不需要开关设备" else acts.joinToString("，")).append("。\n")

        summary?.takeIf { it.isNotBlank() }?.let { append("触发信息：").append(it).append("。\n") }
        append("请用一句话（30字内）温馨、口语化地欢迎主人回家。")
    }

    private data class Environment(
        val temperature: Double?,
        val sunState: String?,
        val acState: String?,
        val lightState: String?,
        val weather: String?
    )
}
