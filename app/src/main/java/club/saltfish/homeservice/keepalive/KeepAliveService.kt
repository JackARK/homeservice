package club.saltfish.homeservice.keepalive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import club.saltfish.homeservice.R
import timber.log.Timber

/**
 * 前台保活服务。常驻通知 + START_STICKY，保证 app 进程长期存活，
 * 从而让 NotificationListener 稳定绑定（避免被 MIUI 后台清理）。
 */
class KeepAliveService : Service() {

    companion object {
        private const val CHANNEL_ID = "keepalive"
        private const val NOTIF_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(NOTIF_ID, buildNotification())
        Timber.i("KeepAlive 前台服务已启动")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // START_STICKY：服务被杀后系统自动重建
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "保活服务",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "保持 homeService 后台运行" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("homeService 运行中")
            .setContentText("通知监听服务后台运行")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
}
