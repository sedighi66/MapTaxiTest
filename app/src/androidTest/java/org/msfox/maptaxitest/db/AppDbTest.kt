package org.msfox.maptaxitest.db

import org.junit.Test
import org.junit.Assert.*

/**
 * Created by mohsen on 04,July,2020
 */
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.msfox.maptaxitest.model.Vehicle
import java.util.concurrent.TimeUnit

abstract class AppDbTest {

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()
    private lateinit var _db: AppDb
    val db: AppDb
        get() = _db

    val dao: VehicleDao
        get() = _db.vehicleDao()

    @Before
    fun initDb() {
        _db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDb::class.java
        ).build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        _db.close()
    }
}