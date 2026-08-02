package club.saltfish.homeservice.ha

/**
 * Home Assistant REST API 客户端。
 *
 * HA 部署在盒子上（默认 http://<box>:8123），用于控制接入 HA 的米家等设备。
 * 端点参考：
 * - POST /api/services/<domain>/<service>  调用服务（控制设备的核心）
 * - GET  /api/states/<entity_id>          查询实体状态
 * - GET  /api/                            健康检查（需 token）
 *
 * 所有方法均为 suspend，返回 [Result] 携带成功/失败信息。
 */
interface HomeAssistantClient {
    /** 打开实体（调用 <entityId 前缀域>.turn_on） */
    suspend fun turnOn(entityId: String): Result<Unit>

    /** 关闭实体（调用 <前缀域>.turn_off） */
    suspend fun turnOff(entityId: String): Result<Unit>

    /** 切换实体（调用 <前缀域>.toggle） */
    suspend fun toggle(entityId: String): Result<Unit>

    /** 调用任意服务（最通用入口，serviceData 如 {"entity_id":"light.x"}） */
    suspend fun callService(
        domain: String,
        service: String,
        serviceData: Map<String, Any?> = emptyMap()
    ): Result<Unit>

    /** 查询实体状态，成功返回 state 字符串（如 "on"/"off"） */
    suspend fun getState(entityId: String): Result<String>

    /** 健康检查，返回 HA 是否可达且 token 有效 */
    suspend fun health(): Boolean
}
