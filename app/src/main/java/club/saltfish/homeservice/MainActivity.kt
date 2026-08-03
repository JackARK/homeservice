package club.saltfish.homeservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import club.saltfish.homeservice.root.RootShell
import timber.log.Timber

/**
 * 应用入口界面。
 *
 * - 内嵌 HTTP 服务器存活时：以 WebView 加载本机 Web 管理端（`http://127.0.0.1:<port>`），
 *   通过 query 注入 token 免密进入——APP 页面与 Web 管理端共用同一套 SPA，零重复开发。
 * - 服务器未运行（端口占用等启动失败）时：降级为原生状态页，提示排查。
 * - 另外负责请求 POST_NOTIFICATIONS 权限（Android 13+，常驻通知依赖）。
 */
class MainActivity : AppCompatActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        val app = application as App
        if (app.isHttpServerRunning() && !app.serverToken.isNullOrBlank()) {
            showWebConsole(app)
        } else {
            Timber.w("内嵌服务器未运行，WebView 降级为原生状态页")
            showFallbackStatus(app)
        }
        Timber.i("MainActivity 启动")
    }

    /** WebView 形态：加载本机 SPA，仅放行 127.0.0.1，拦截一切外部跳转 */
    @SuppressLint("SetJavaScriptEnabled")
    private fun showWebConsole(app: App) {
        val port = app.config.server.port
        val view = WebView(this)
        view.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true      // SPA 用 localStorage 存 token
            allowFileAccess = false
            allowContentAccess = false
        }
        view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean =
                url?.startsWith("http://127.0.0.1:$port") != true // 非本机地址一律拦截
        }
        setContentView(view)
        webView = view

        // 返回键优先回退 WebView 历史（hash 路由页面间导航）
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val wv = webView
                if (wv != null && wv.canGoBack()) wv.goBack() else finish()
            }
        })

        // token 经 query 传给 SPA，SPA 读取后写入 localStorage 并清掉地址参数
        view.loadUrl("http://127.0.0.1:$port/?token=${app.serverToken}#/")
    }

    /** 降级形态：原生状态页（服务器启动失败时排查用） */
    private fun showFallbackStatus(app: App) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 96, 64, 64)
        }
        val textView = TextView(this).apply {
            textSize = 15f
            text = buildString {
                append("homeService 运行中（内嵌服务器未启动）\n\n")
                append("root 可用：${RootShell.isAvailable()}\n")
                append("规则数：${app.config.rules.size}\n")
                app.config.rules.forEach { append("  • ${it.id}（${it.packageNames.joinToString()}）\n") }
                append("\nbridge：${app.config.bridge.baseUrl}\n")
                append("\n若通知不生效，请检查：\n")
                append("  1. 设置 → 通知访问权限 → homeService\n")
                append("  2. 设置 → 应用 → homeService → 自启动\n")
            }
        }
        val settingsButton = Button(this).apply {
            text = "打开通知访问权限设置"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        layout.addView(textView)
        layout.addView(settingsButton)
        setContentView(layout)
    }

    override fun onDestroy() {
        webView?.destroy()
        webView = null
        super.onDestroy()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }
}
