package club.saltfish.homeservice.server

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRateLimiterTest {

    private var now = 1_000_000L
    private val limiter = AuthRateLimiter(maxFailures = 3, lockoutMs = 60_000L) { now }

    @Test
    fun `未达阈值不锁定`() {
        repeat(2) { limiter.onFailure("1.1.1.1") }
        assertFalse(limiter.isLocked("1.1.1.1"))
    }

    @Test
    fun `连续失败达阈值后锁定`() {
        repeat(3) { limiter.onFailure("2.2.2.2") }
        assertTrue(limiter.isLocked("2.2.2.2"))
    }

    @Test
    fun `锁定期过后自动解除`() {
        repeat(3) { limiter.onFailure("3.3.3.3") }
        assertTrue(limiter.isLocked("3.3.3.3"))
        now += 61_000L
        assertFalse(limiter.isLocked("3.3.3.3"))
    }

    @Test
    fun `鉴权成功清除失败记录`() {
        repeat(2) { limiter.onFailure("4.4.4.4") }
        limiter.onSuccess("4.4.4.4")
        repeat(2) { limiter.onFailure("4.4.4.4") }
        assertFalse(limiter.isLocked("4.4.4.4")) // 重新计数，未达阈值
    }

    @Test
    fun `不同 IP 独立计数`() {
        repeat(3) { limiter.onFailure("5.5.5.5") }
        assertTrue(limiter.isLocked("5.5.5.5"))
        assertFalse(limiter.isLocked("6.6.6.6"))
    }
}
