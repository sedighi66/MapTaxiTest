package org.msfox.maptaxitest.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.msfox.maptaxitest.api.SnappService
import org.msfox.maptaxitest.db.VehicleDao
import org.msfox.maptaxitest.model.Document
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.utils.ApiUtil.successCall
import org.msfox.maptaxitest.utils.MainCoroutineRule
import org.msfox.maptaxitest.utils.getOrAwaitValue

/**
 * Created by mohsen on 04,July,2020
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RepoRepositoryTest {
    private lateinit var repo: VehicleRepository
    private val dao = Mockito.mock(VehicleDao::class.java)
    private val service = Mockito.mock(SnappService::class.java)

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        repo = VehicleRepository(
            coroutineRule.getAppCoroutineDispatchers(),
            vehicleDao = dao, snappService = service)
    }

    @Test
    fun `load vehicles in offline mode`() = coroutineRule.runBlockingTest {

        val data = MutableLiveData<List<Vehicle>>()
        val list = mutableListOf<Vehicle>()
        data.postValue(list)
        `when`(dao.getAll()).thenReturn(data)

        val vehicles = repo.loadVehicles(true).getOrAwaitValue()
        Mockito.verify(dao).getAll()
        Mockito.verifyNoMoreInteractions(dao)

        assertThat<Status>(vehicles.status, CoreMatchers.`is`(Status.SUCCESS))
        assertThat<String>(vehicles.message, CoreMatchers.nullValue())
        Assert.assertNotNull(vehicles.data)
        assertThat<Int>(vehicles.data!!.size, CoreMatchers.`is`(0))

        list.add(createVehicle(11))
        list.add(createVehicle(22))
        list.add(createVehicle(33))
        assertThat<Int>(vehicles.data!!.size, CoreMatchers.`is`(3))
    }

    @Test
    fun `load vehicles in online mode`() = coroutineRule.runBlockingTest {

        val data = MutableLiveData<List<Vehicle>>()
        val list = mutableListOf<Vehicle>()
        data.postValue(list)
        `when`(dao.getAll()).thenReturn(data)

        val document = Document(list)
        val call = successCall(document)
        `when`(service.getVehicles()).thenReturn(call)


        val vehicles = repo.loadVehicles(false).getOrAwaitValue()
        Mockito.verify(service).getVehicles()
        Mockito.verifyNoMoreInteractions(service)

        assertThat<Status>(vehicles.status, CoreMatchers.`is`(Status.SUCCESS))
        assertThat<String>(vehicles.message, CoreMatchers.nullValue())
        Assert.assertNotNull(vehicles.data)
        assertThat<Int>(vehicles.data!!.size, CoreMatchers.`is`(0))

        list.add(createVehicle(11))
        list.add(createVehicle(22))
        list.add(createVehicle(33))
        assertThat<Int>(vehicles.data!!.size, CoreMatchers.`is`(3))
    }

    private fun createVehicle(bearing: Int, type: String = "ECO"): Vehicle =
        Vehicle(type, 0.0, 0.0, bearing, "")
}