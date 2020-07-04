package org.msfox.maptaxitest.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.msfox.maptaxitest.model.Vehicle
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

//TODO: this extension should be removed. Instead, we should use utils
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

/**
 * Created by mohsen on 04,July,2020
 */
@RunWith(AndroidJUnit4::class)
class VehicleDaoTest : AppDbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndLoad() {
        val user = createUser(11)
        dao.insert(user)

        val loadedVehicles = dao.getAll().getOrAwaitValue()
        MatcherAssert.assertThat(loadedVehicles.count(), CoreMatchers.`is`(1))
        MatcherAssert.assertThat(loadedVehicles.first(), CoreMatchers.`is`(user))
        MatcherAssert.assertThat(loadedVehicles.first().bearing, CoreMatchers.`is`(11))

        val replacement = createUser(11, "PLUS")
        dao.insert(replacement)

        val loadedVehiclesReplacement = dao.getAll().getOrAwaitValue()
        MatcherAssert.assertThat(loadedVehiclesReplacement.count(), CoreMatchers.`is`(1))
        MatcherAssert.assertThat(loadedVehiclesReplacement.first(), CoreMatchers.`is`(replacement))
        MatcherAssert.assertThat(loadedVehiclesReplacement.first().type, CoreMatchers.`is`("PLUS"))
    }

    private fun createUser(bearing: Int, type: String = "ECO") =
        Vehicle(type, 0.0, 0.0, bearing, "")
}