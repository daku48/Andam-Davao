package com.example.andamdavao

import com.example.andamdavao.utils.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeUtilsTest {
    private val min = 60_000L

    @Test
    fun formatsRelativeTimes() {
        val now = 10_000_000_000L
        assertEquals("just now", TimeUtils.formatAgo(now - 20_000L, now))
        assertEquals("5 min ago", TimeUtils.formatAgo(now - 5 * min, now))
        assertEquals("1 hour ago", TimeUtils.formatAgo(now - 60 * min, now))
        assertEquals("3 hours ago", TimeUtils.formatAgo(now - 180 * min, now))
        assertEquals("1 day ago", TimeUtils.formatAgo(now - 24 * 60 * min, now))
        assertEquals("4 days ago", TimeUtils.formatAgo(now - 4 * 24 * 60 * min, now))
    }

    @Test
    fun futureTimestampDoesNotGoNegative() {
        assertEquals("just now", TimeUtils.formatAgo(2_000L, 1_000L))
    }
}
