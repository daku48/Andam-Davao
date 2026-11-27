package com.example.andamdavao.utils

object TimeUtils {
    fun formatAgo(thenMillis: Long, nowMillis: Long = System.currentTimeMillis()): String {
        val minutes = (nowMillis - thenMillis).coerceAtLeast(0L) / 60_000L
        return when {
            minutes < 1 -> "just now"
            minutes < 60 -> "$minutes min ago"
            minutes < 60 * 24 -> {
                val hours = minutes / 60
                if (hours == 1L) "1 hour ago" else "$hours hours ago"
            }
            else -> {
                val days = minutes / (60 * 24)
                if (days == 1L) "1 day ago" else "$days days ago"
            }
        }
    }
}
