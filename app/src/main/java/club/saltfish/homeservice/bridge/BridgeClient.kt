package club.saltfish.homeservice.bridge

/**
 * open-xiaoai-bridge 的 HTTP API 客户端。
 *
 * bridge 部署在盒子上（默认 http://<box>:9092），端点参考：
 * - POST /api/tts/doubao  豆包 TTS 合成并播放（[playText] 在配置了 ttsSpeaker 时走此端点，指定音色）
 * - POST /api/play/text   播放文字（小爱原生 TTS，[playText] 未配置音色时回退到此端点）
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

    /**
     * 获取可用音色列表（来自 bridge 的 GET /api/tts/doubao_voices，即火山音色库）。
     * 返回 key=音色ID（如 zh_female_vv_uranus_bigtts）、value=显示名 的映射。
     */
    suspend fun listVoices(): Result<Map<String, String>>
}
