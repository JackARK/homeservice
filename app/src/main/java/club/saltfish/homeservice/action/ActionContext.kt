package club.saltfish.homeservice.action

/**
 * 动作执行上下文。携带触发该批动作的通知信息，供需要上下文的动作
 * （如 welcomeHome 的 LLM 播报）使用。
 *
 * @param triggerTimeMs 触发时间（通常是通知的 postTime）
 * @param notificationSummary 通知摘要（"标题 正文"），可空
 */
data class ActionContext(
    val triggerTimeMs: Long,
    val notificationSummary: String?
)
