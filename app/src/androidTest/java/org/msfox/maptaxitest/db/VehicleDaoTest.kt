package org.msfox.maptaxitest.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.utils.getOrAwaitValue

/**
 * Created by mohsen on 04,July,2020
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class VehicleDaoTest : AppDbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndLoad() = runBlockingTest {
        //insert
        val user = createUser(11)
        dao.insert(user)

        val loadedVehicles = dao.getAll().getOrAwaitValue()
        MatcherAssert.assertThat(loadedVehicles.count(), CoreMatchers.`is`(1))
        MatcherAssert.assertThat(loadedVehicles.first(), CoreMatchers.`is`(user))
        MatcherAssert.assertThat(loadedVehicles.first().bearing, CoreMatchers.`is`(11))

        //replacement
        val replacement = createUser(11, "PLUS")
        dao.insert(replacement)

        val loadedVehiclesReplacement = dao.getAll().getOrAwaitValue()
        MatcherAssert.assertThat(loadedVehiclesReplacement.count(), CoreMatchers.`is`(1))
        MatcherAssert.assertThat(loadedVehiclesReplacement.first(), CoreMatchers.`is`(replacement))
        MatcherAssert.assertThat(loadedVehiclesReplacement.first().type, CoreMatchers.`is`("PLUS"))

        //deleteAll
        dao.deleteAll()
        val loadAll = dao.getAll().getOrAwaitValue()
        MatcherAssert.assertThat(loadAll.count(), CoreMatchers.`is`(0))
    }

    private fun createUser(bearing: Int, type: String = "ECO") =
        Vehicle(type, 0.0, 0.0, bearing, "")
}