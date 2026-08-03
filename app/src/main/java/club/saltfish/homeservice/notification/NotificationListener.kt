package club.saltfish.homeservice.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import club.saltfish.homeservice.App
import club.saltfish.homeservice.action.ActionContext
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 通知监听服务。系统在新通知到达时回调，解析后交给规则引擎匹配，
 * 命中的动作通过 [App.applicationScope] 异步分发执行，并携带触发上下文（时间、通知摘要）。
 *
 * 需要用户在「设置 → 通知访问权限」授权，或由 root 自动授权
 * （`cmd notification allow_listener <pkg>/<本类全路径>`）。
 */
class NotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        Timber.i("通知监听已连接")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        val ticker = notification.tickerText?.toString()

        val parsed = NotificationParser.parse(
            packageName = sbn.packageName,
            title = title,
            text = text,
            ticker = ticker,
            postTime = sbn.postTime
        )
        Timber.i("收到通知 pkg=${parsed.packageName} title=${parsed.title} text=${parsed.text}")

        val app = applicationContext as App
        val matched = app.ruleEngine.matchedRules(parsed, app.config.rules)
        RecentEvents.add(
            NotificationEvent(
                timeMillis = sbn.postTime,
                packageName = parsed.packageName,
                title = parsed.title,
                text = parsed.text,
                matchedRuleIds = matched.map { it.id }
            )
        )
        val actions = matched.flatMap { it.actions }
        if (actions.isEmpty()) return

        Timber.i("规则命中，待执行动作 ${actions.size} 个")
        val summary = listOfNotNull(parsed.title, parsed.text).joinToString(" ").takeIf { it.isNotBlank() }
        app.applicationScope.launch {
            app.actionDispatcher.dispatch(actions, ActionContext(sbn.postTime, summary))
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // 不处理通知移除事件
    }
}
