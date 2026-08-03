package club.saltfish.homeservice.config

/**
 * 配置脱敏（纯函数，可独立单测）。
 *
 * Web 管理端 GET /config 时，敏感字段（bridge/HA/server 的 token、LLM apiKey）
 * 以 [MASK] 掩码返回，防止经内网穿透泄漏；POST /config 回传时，
 * 值仍为 [MASK] 的字段视为「未修改」，用旧值还原（[mergeSecrets]）。
 */
object Redaction {

    const val MASK = "********"

    /** 返回脱敏后的配置副本（原配置不变） */
    fun redact(config: AppConfig): AppConfig = config.copy(
        bridge = config.bridge.copy(token = mask(config.bridge.token)),
        ha = config.ha.copy(token = mask(config.ha.token)),
        llm = config.llm.copy(apiKey = mask(config.llm.apiKey)),
        server = config.server.copy(token = mask(config.server.token))
    )

    /** POST 回来的配置中仍为掩码的敏感字段，用 [old] 中的原值还原 */
    fun mergeSecrets(new: AppConfig, old: AppConfig): AppConfig = new.copy(
        bridge = new.bridge.copy(token = unmask(new.bridge.token, old.bridge.token)),
        ha = new.ha.copy(token = unmask(new.ha.token, old.ha.token)),
        llm = new.llm.copy(apiKey = unmask(new.llm.apiKey, old.llm.apiKey)),
        server = new.server.copy(token = unmask(new.server.token, old.server.token))
    )

    private fun mask(value: String): String = if (value.isBlank()) value else MASK

    private fun unmask(new: String, old: String): String = if (new == MASK) old else new
}
