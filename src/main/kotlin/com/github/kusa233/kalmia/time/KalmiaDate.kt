package com.github.kusa233.kalmia.time

object KalmiaDate {
    const val SECOND = 1000L
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR
    const val MONTH = 30 * DAY
    const val YEAR = 365 * MONTH

    fun formatTime(time: Long): String {
        var remaining = time

        val years = remaining / YEAR
        remaining %= YEAR

        val months = remaining / MONTH
        remaining %= MONTH

        val days = remaining / DAY
        remaining %= DAY

        val hours = remaining / HOUR
        remaining %= HOUR

        val minutes = remaining / MINUTE
        remaining %= MINUTE

        val seconds = remaining / SECOND
        val ms = remaining % SECOND

        return buildString {
            if (years > 0) append("${years}y, ")
            if (months > 0) append("${months}m, ")
            if (days > 0) append("${days}d, ")
            if (hours > 0) append("${hours}h, ")
            if (minutes > 0) append("${minutes}m, ")
            if (seconds > 0) append("${seconds}s, ")
            if (ms > 0) append("${ms}ms")
        }
    }
}