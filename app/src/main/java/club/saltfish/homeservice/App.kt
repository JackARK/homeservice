package club.saltfish.homeservice

import android.content.Intent
import android.app.Application
import androidx.core.content.ContextCompat
import club.saltfish.homeservice.action.ActionDispatcher
import club.saltfish.homeservice.bridge.BridgeClient
import club.saltfish.homeservice.bridge.OkHttpBridgeClient
import club.saltfish.homeservice.config.AppConfig
import club.saltfish.homeservice.config.ConfigManager
import club.saltfish.homeservice.ha.HomeAssistantClient
import club.saltfish.homeservice.ha.OkHttpHomeAssistantClient
import club.saltfish.homeservice.keepalive.KeepAliveService
import club.saltfish.homeservice.llm.DeepSeekClient
import club.saltfish.homeservice.llm.LlmClient
import club.saltfish.homeservice.log.RingBufferTree
import club.saltfish.homeservice.log.RingLogBuffer
import club.saltfish.homeservice.notification.NotificationListener
import club.saltfish.homeservice.root.RootShell
import club.saltfish.homeservice.rule.RuleEngine
import club.saltfish.homeservice.server.HttpServer
import club.saltfish.homeservice.smart.WelcomeHomeOrchestrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.SecureRandom

/**
 * 应用入口。初始化全链路依赖：配置 → bridge/HA/LLM → 智能编排 → 动作分发 → 规则引擎，
 * 启动保活前台服务与内嵌 HTTP 服务器，并尝试 root 自动授权。
 */
class App : Application() {

    companion object {
        /** 端口切换前的延迟（毫秒），让旧实例上正在处理的响应先发完 */
        private const val RESTART_DELAY_MS = 500L
    }

    /** 进程启动时间（/api/status 的 uptime 用） */
    val startTimeMillis: Long = System.currentTimeMillis()

    /** 内存日志环形缓冲（Web 管理端日志查看的数据源，RELEASE 也 plant） */
    val ringLogBuffer: RingLogBuffer = RingLogBuffer()

    /** 应用级协程作用域；SupervisorJob 保证单个子协程失败不会取消兄弟协程 */
    val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    lateinit var config: AppConfig
        private set
    lateinit var bridgeClient: BridgeClient
        private set
    lateinit var haClient: HomeAssistantClient
        private set
    lateinit var llmClient: LlmClient
        private set
    lateinit var welcomeHomeOrchestrator: WelcomeHomeOrchestrator
        private set
    lateinit var actionDispatcher: ActionDispatcher
        private set
    private var httpServer: HttpServer? = null

    /** 内嵌服务器实际使用的 token（可能为首启自动生成），供 MainActivity WebView 免密进入 */
    var serverToken: String? = null
        private set

    /** 内嵌 HTTP 服务器是否存活 */
    fun isHttpServerRunning(): Boolean = httpServer?.isAlive == true

    val ruleEngine: RuleEngine = RuleEngine(
        errorHandler = { ruleId, error -> Timber.w(error, "规则 $ruleId 正则无效，已跳过") }
    )

    /** 重新加载配置（HTTP API 热更新规则后调用） */
    fun reloadConfig() {
        val oldServer = if (::config.isInitialized) config.server else null
        config = ConfigManager.load(this)
        ensureServerToken()
        bridgeClient = OkHttpBridgeClient(config.bridge)
        haClient = OkHttpHomeAssistantClient(config.ha)
        llmClient = DeepSeekClient(config.llm)
        welcomeHomeOrchestrator = WelcomeHomeOrchestrator(haClient, bridgeClient, llmClient, config.smartHome)
        actionDispatcher = ActionDispatcher(bridgeClient, haClient, welcomeHomeOrchestrator::welcomeHome)
        ruleEngine.resetDedup()
        Timber.i("配置已重新加载，规则数=${config.rules.size}")

        // server 配置热生效：token 由 HttpServer 动态读取（即时生效，无需重启）；
        // 仅端口变化需要换实例，走「先起新、后停旧」的零停机流程
        if (oldServer != null && oldServer.port != config.server.port) {
            scheduleHttpServerRestart()
        } else if (oldServer != null && oldServer.token != config.server.token) {
            Timber.i("server token 已变更，即时生效（无需重启服务器）")
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 环形缓冲树 RELEASE 也 plant（Web 日志查看依赖）；DEBUG 额外输出 logcat
        Timber.plant(RingBufferTree(ringLogBuffer))
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        config = ConfigManager.load(this)
        bridgeClient = OkHttpBridgeClient(config.bridge)
        haClient = OkHttpHomeAssistantClient(config.ha)
        llmClient = DeepSeekClient(config.llm)
        welcomeHomeOrchestrator = WelcomeHomeOrchestrator(haClient, bridgeClient, llmClient, config.smartHome)
        actionDispatcher = ActionDispatcher(bridgeClient, haClient, welcomeHomeOrchestrator::welcomeHome)
        Timber.i("homeService 应用启动，规则数=${config.rules.size}")

        // 启动保活前台服务
        ContextCompat.startForegroundService(this, Intent(this, KeepAliveService::class.java))

        // 启动内嵌 HTTP 服务器
        startHttpServer()

        // root 自动授权（异步，避免阻塞 onCreate）
        applicationScope.launch {
            if (RootShell.isAvailable()) {
                val component = "$packageName/${NotificationListener::class.java.name}"
                RootShell.allowNotificationListener(component)
                RootShell.addToBatteryWhitelist(packageName)
            } else {
                Timber.w("root 不可用，需手动授权通知监听与电池白名单")
            }
        }
    }

    /**
     * 延迟执行端口切换（配置热重载）。
     *
     * 端口切换通常由「POST /api/config 修改了端口」触发，调用方正处在旧实例的
     * 请求处理线程里——延迟 500ms 让响应发完后再停旧实例。
     * 注意：延迟只影响旧实例退出时机，新端口在 [restartHttpServer] 里先绑定成功，即刻可用。
     */
    private fun scheduleHttpServerRestart() {
        applicationScope.launch {
            delay(RESTART_DELAY_MS)
            restartHttpServer()
        }
    }

    /**
     * 端口切换：先在新端口启动新实例（绑定成功即刻可用），再停旧实例——零停机。
     * 新端口绑定失败时保留旧实例继续服务。
     */
    @Synchronized
    private fun restartHttpServer() {
        Timber.i("server 端口已变更，切换到新端口 ${config.server.port}")
        val old = httpServer
        val newServer = try {
            createHttpServer().also { it.start() }
        } catch (e: Exception) {
            Timber.e(e, "新端口 ${config.server.port} 启动失败，保留旧实例继续服务")
            return
        }
        httpServer = newServer
        Timber.i("HTTP 服务器已在新端口 ${config.server.port} 启动")
        try {
            old?.stop()
            Timber.i("旧端口实例已停止")
        } catch (e: Exception) {
            Timber.w(e, "停止旧 HTTP 服务器实例异常")
        }
    }

    private fun startHttpServer() {
        try {
            ensureServerToken()
            httpServer = createHttpServer().also {
                it.start()
                Timber.i("HTTP 服务器已启动，端口 ${config.server.port}")
            }
        } catch (e: Exception) {
            // 端口占用等异常：记日志，不影响其它功能
            Timber.e(e, "HTTP 服务器启动失败")
        }
    }

    /** 构造服务器实例（token 由实例运行期动态读取 [serverToken]，不做构造参数） */
    private fun createHttpServer(): HttpServer =
        HttpServer(this, config.server.port, ringLogBuffer)

    /**
     * 确保 server token 非空并同步到 [serverToken]：
     * 为空（首启或用户清空）时生成 48 位随机 token 持久化，保证管理接口永不裸奔。
     */
    private fun ensureServerToken() {
        var token = config.server.token
        if (token.isBlank()) {
            token = generateToken()
            config = config.copy(server = config.server.copy(token = token))
            ConfigManager.save(this, config)
            Timber.i("HTTP 服务器 token 自动生成（仅此次明文展示，请妥善保存）：$token")
        }
        serverToken = token
    }

    /** 生成 48 位十六进制随机 token（SecureRandom） */
    private fun generateToken(): String {
        val bytes = ByteArray(24)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
