package club.saltfish.homeservice.root

import com.topjohnwu.superuser.Shell
import timber.log.Timber

/**
 * Root 操作集中封装（AGENTS.md §6.3：所有 root 操作在此模块，其它模块不直接 su）。
 * 基于 libsu。
 *
 * 注意：传入的命令参数均来自代码常量，不拼接外部输入，避免命令注入（§6.2）。
 */
object RootShell {

    /** root（Magisk）是否可用 */
    fun isAvailable(): Boolean = try {
        Shell.getShell().isRoot
    } catch (e: Exception) {
        Timber.w(e, "root 不可用")
        false
    }

    /** 自动授权通知监听服务（component 格式：pkg/pkg.ServiceClass） */
    fun allowNotificationListener(component: String): Boolean {
        val result = Shell.cmd("cmd notification allow_listener $component").exec()
        Timber.i("allow_listener $component => ${if (result.isSuccess) "成功" else "失败"}")
        return result.isSuccess
    }

    /** 加入电池优化白名单（免 doze 限制） */
    fun addToBatteryWhitelist(pkg: String): Boolean {
        val result = Shell.cmd("dumpsys deviceidle whitelist +$pkg").exec()
        Timber.i("battery whitelist $pkg => ${if (result.isSuccess) "成功" else "失败"}")
        return result.isSuccess
    }
}
