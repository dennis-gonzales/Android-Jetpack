package com.dnnsgnzls.jetpack.util

import java.util.concurrent.TimeUnit

fun Long.hasElapsed(nanoseconds: Long): Boolean {
    val currentTime = System.nanoTime()
    val elapsedTime = currentTime - this
    return elapsedTime >= nanoseconds
}

fun Long.timeLeft(refreshTime: Long): String {
    val elapsedTime = System.nanoTime() - this
    var remainingTime = refreshTime - elapsedTime

    if (remainingTime <= 0) {
        return "No time left"
    }

    remainingTime = TimeUnit.NANOSECONDS.toSeconds(remainingTime)

    val days = TimeUnit.SECONDS.toDays(remainingTime).also { remainingTime -= TimeUnit.DAYS.toSeconds(it) }
    val hours = TimeUnit.SECONDS.toHours(remainingTime).also { remainingTime -= TimeUnit.HOURS.toSeconds(it) }
    val minutes = TimeUnit.SECONDS.toMinutes(remainingTime).also { remainingTime -= TimeUnit.MINUTES.toSeconds(it) }
    val seconds = remainingTime

    return buildString {
        if (days > 0) append("$days days, ")
        if (hours > 0) append("$hours hours, ")
        if (minutes > 0) append("$minutes mins, ")
        if (seconds > 0) append("$seconds secs")
    }.trimEnd(',', ' ')
}
