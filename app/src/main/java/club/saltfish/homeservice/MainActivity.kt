package club.saltfish.homeservice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import club.saltfish.homeservice.root.RootShell
import timber.log.Timber

/**
 * 应用入口界面。用于：
 * - 请求 POST_NOTIFICATIONS 权限（Android 13+，常驻通知依赖）
 * - 从桌面启动 app（MIUI 识别为"用户使用过"，放宽后台限制）
 * - 展示当前配置与运行状态
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        val app = application as App
        val textView = TextView(this).apply {
            setPadding(64, 96, 64, 64)
            textSize = 15f
            text = buildString {
                append("homeService 运行中\n\n")
                append("root 可用：${RootShell.isAvailable()}\n")
                append("规则数：${app.config.rules.size}\n")
                app.config.rules.forEach { append("  • ${it.id}（${it.packageNames.joinToString()}）\n") }
                append("\nbridge：${app.config.bridge.baseUrl}\n")
                append("\n若通知不生效，请检查：\n")
                append("  1. 设置 → 通知访问权限 → homeService\n")
                append("  2. 设置 → 应用 → homeService → 自启动\n")
            }
        }
        setContentView(textView)
        Timber.i("MainActivity 启动")
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }
}
