package club.saltfish.homeservice.keepalive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import timber.log.Timber

/** 开机自启：启动保活前台服务 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Timber.i("收到开机广播，启动 KeepAliveService")
            ContextCompat.startForegroundService(
                context,
                Intent(context, KeepAliveService::class.java)
            )
        }
    }
}
