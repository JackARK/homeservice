package club.saltfish.homeservice.server

/**
 * 鉴权失败限流器（纯逻辑，可独立单测）。
 *
 * 内网穿透后管理接口面向公网，防止 token 被爆破：
 * 同一来源 IP 连续失败 [maxFailures] 次后锁定 [lockoutMs]，
 * 锁定期间所有请求直接拒绝（包括 token 正确的）。
 */
class AuthRateLimiter(
    private val maxFailures: Int = 10,
    private val lockoutMs: Long = 5 * 60_000L,
    private val now: () -> Long = System::currentTimeMillis
) {

    private class Record(var failures: Int, var lockedUntil: Long)

    private val records = mutableMapOf<String, Record>()

    /** 该 IP 当前是否处于锁定期 */
    @Synchronized
    fun isLocked(ip: String): Boolean {
        val record = records[ip] ?: return false
        if (record.lockedUntil <= now()) {
            // 锁定期已过，清除记录重新开始
            records.remove(ip)
            return false
        }
        return true
    }

    /** 记录一次鉴权失败；达到阈值进入锁定 */
    @Synchronized
    fun onFailure(ip: String) {
        val record = records.getOrPut(ip) { Record(0, 0L) }
        record.failures++
        if (record.failures >= maxFailures) {
            record.lockedUntil = now() + lockoutMs
        }
    }

    /** 鉴权成功，清除该 IP 的失败记录 */
    @Synchronized
    fun onSuccess(ip: String) {
        records.remove(ip)
    }
}
