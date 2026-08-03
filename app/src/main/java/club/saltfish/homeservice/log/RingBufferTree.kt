package club.saltfish.homeservice.log

import android.util.Log
import timber.log.Timber

/**
 * 把 Timber 日志写入 [RingLogBuffer] 的树。
 *
 * RELEASE 构建也 plant（Web 管理端的日志查看依赖它）；
 * DEBUG 构建额外 plant DebugTree 输出到 logcat。
 */
class RingBufferTree(private val buffer: RingLogBuffer) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val full = if (t != null) "$message\n${Log.getStackTraceString(t)}" else message
        buffer.add(priority, tag, full)
    }
}
