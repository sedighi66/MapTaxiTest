package org.msfox.maptaxitest.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by mohsen on 07,July,2020
 */
class ConvertersTest {

    private var days5: Long = 0
    private var days1: Long = 0

    @Before
    fun setUp() {
        days5 = System.currentTimeMillis() - (5 * 24 * 3600 * 1000)
        days1 = System.currentTimeMillis() - (1 * 24 * 3600 * 1000)

    }

    @Test
    fun milliSecondsToTimesAgo() {
        Assert.assertTrue(Converters.milliSecondsToTimesAgo(days5) == "5 days ago")
        Assert.assertTrue(Converters.milliSecondsToTimesAgo(days1) == "Yesterday")
    }
}