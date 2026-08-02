package club.saltfish.homeservice.bridge

/**
 * open-xiaoai-bridge 的 HTTP API 客户端。
 *
 * bridge 部署在盒子上（默认 http://<box>:9092），端点参考：
 * - POST /api/play/text   播放文字（TTS）
 * - POST /api/play/url    播放音频链接
 * - POST /api/wakeup      唤醒小爱
 * - POST /api/interrupt   打断播放
 * - GET  /api/health      健康检查
 *
 * 所有方法均为 suspend，返回 [Result] 携带成功/失败信息。
 */
interface BridgeClient {
    /** 播放文字（TTS） */
    suspend fun playText(text: String): Result<Unit>

    /** 播放音频链接 */
    suspend fun playUrl(url: String): Result<Unit>

    /** 唤醒小爱音箱 */
    suspend fun wakeup(): Result<Unit>

    /** 打断当前播放 */
    suspend fun interrupt(): Result<Unit>

    /** 健康检查，返回 bridge 是否可达 */
    suspend fun health(): Boolean
}
