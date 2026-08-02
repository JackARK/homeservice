package club.saltfish.homeservice.rule

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DedupTest {

    @Test
    fun firstOccurrenceIsProcessed() {
        var time = 1000L
        val dedup = Dedup { time }
        assertTrue(dedup.shouldProcess("key1", 1000L))
    }

    @Test
    fun secondOccurrenceWithinWindowIsDeduplicated() {
        var time = 1000L
        val dedup = Dedup { time }
        assertTrue(dedup.shouldProcess("key1", 1000L))
        time = 1500L
        assertFalse(dedup.shouldProcess("key1", 1000L))
    }

    @Test
    fun occurrenceAfterWindowIsProcessed() {
        var time = 1000L
        val dedup = Dedup { time }
        assertTrue(dedup.shouldProcess("key1", 1000L))
        time = 2100L
        assertTrue(dedup.shouldProcess("key1", 1000L))
    }

    @Test
    fun differentKeysAreIndependent() {
        var time = 1000L
        val dedup = Dedup { time }
        assertTrue(dedup.shouldProcess("key1", 1000L))
        assertTrue(dedup.shouldProcess("key2", 1000L))
    }

    @Test
    fun clearResetsAllKeys() {
        var time = 1000L
        val dedup = Dedup { time }
        dedup.shouldProcess("key1", 1000L)
        dedup.clear()
        assertTrue(dedup.shouldProcess("key1", 1000L))
    }
}
