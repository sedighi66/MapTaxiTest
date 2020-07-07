package org.msfox.maptaxitest.utils

import kotlin.math.floor

/**
 * Created by mohsen on 07,July,2020
 */
object Converters {

    fun milliSecondsToTimesAgo(timeMilliSeconds: Long): String {
        val deltaTimeMilliSeconds = (System.currentTimeMillis() - timeMilliSeconds)

        val days = floor(deltaTimeMilliSeconds.toDouble() / (1000 * 3600 * 24)).toInt()
        if(days > 1) return "$days days ago"
        if(days > 0) return "Yesterday"

        val hours = floor(deltaTimeMilliSeconds.toDouble() / (1000 * 3600)).toInt()
        if(hours > 1) return "$hours hours ago"
        if(hours > 0) return "An hour ago"

        val minutes = floor(deltaTimeMilliSeconds.toDouble() / (1000 * 60))
        if(minutes > 1) return "$minutes minutes ago"
        if(minutes > 0) return "A minute ago"

        return "Right NOW"
    }
}