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
import club.saltfish.homeservice.notification.NotificationListener
import club.saltfish.homeservice.root.RootShell
import club.saltfish.homeservice.rule.RuleEngine
import club.saltfish.homeservice.server.HttpServer
import club.saltfish.homeservice.smart.WelcomeHomeOrchestrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 应用入口。初始化全链路依赖：配置 → bridge/HA/LLM → 智能编排 → 动作分发 → 规则引擎，
 * 启动保活前台服务与内嵌 HTTP 服务器，并尝试 root 自动授权。
 */
class App : Application() {

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

    val ruleEngine: RuleEngine = RuleEngine(
        errorHandler = { ruleId, error -> Timber.w(error, "规则 $ruleId 正则无效，已跳过") }
    )

    /** 重新加载配置（HTTP API 热更新规则后调用） */
    fun reloadConfig() {
        config = ConfigManager.load(this)
        bridgeClient = OkHttpBridgeClient(config.bridge)
        haClient = OkHttpHomeAssistantClient(config.ha)
        llmClient = DeepSeekClient(config.llm)
        welcomeHomeOrchestrator = WelcomeHomeOrchestrator(haClient, bridgeClient, llmClient, config.smartHome)
        actionDispatcher = ActionDispatcher(bridgeClient, haClient, welcomeHomeOrchestrator::welcomeHome)
        ruleEngine.resetDedup()
        Timber.i("配置已重新加载，规则数=${config.rules.size}")
    }

    override fun onCreate() {
        super.onCreate()
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

    private fun startHttpServer() {
        try {
            httpServer = HttpServer(this, config.server.port, config.server.token).also {
                it.start()
                Timber.i("HTTP 服务器已启动，端口 ${config.server.port}")
            }
        } catch (e: Exception) {
            // 端口占用等异常：记日志，不影响其它功能
            Timber.e(e, "HTTP 服务器启动失败")
        }
    }
}
