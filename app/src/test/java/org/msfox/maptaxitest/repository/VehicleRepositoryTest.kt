package org.msfox.maptaxitest.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.*
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
        `when`(dao.getAll()).thenReturn(data)

        val document = Document(list)
        val call = successCall(document)
        `when`(service.getVehicles()).thenReturn(call)

        val vehicles = repo.loadVehicles(true)
        Mockito.verify(dao).getAll()
        Mockito.verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Vehicle>>>>()
        vehicles.observeForever(observer)
        Mockito.verifyNoMoreInteractions(service)
        Mockito.verify(observer).onChanged(Resource.loading(null))

        data.postValue(list)
        Mockito.verify(observer).onChanged(Resource.success(list))
        Mockito.verify(dao).getAll()
        Mockito.verifyNoMoreInteractions(service)

        list.add(createVehicle(1))
        Mockito.verify(observer).onChanged(Resource.success(list))
        Mockito.verify(dao).getAll()
        Mockito.verifyNoMoreInteractions(service)
    }

    @Test
    fun `load vehicles in online mode`() = coroutineRule.runBlockingTest {

        val data = MutableLiveData<List<Vehicle>>()
        val list = mutableListOf(createVehicle(1))
        `when`(dao.getAll()).thenReturn(data)

        val document = Document(list)
        val call = successCall(document)
        `when`(service.getVehicles()).thenReturn(call)

        val vehicles = repo.loadVehicles(false)
        val observer = mock<Observer<Resource<List<Vehicle>>>>()
        vehicles.observeForever(observer)
        Mockito.verify(observer).onChanged(Resource.loading(null))

        //no interactions with service yet, because in liveData, we have to post a null
        //object in other word, we notify add observers that there is no data in database.
        Mockito.verifyNoMoreInteractions(service)

        //when we get data from internet, firstly, we should verify that the data is inserted to db
        //secondly, we should check that the data is available in observers.
        //because of the reason that database is the only place that we observe data,
        //we should simulate it by next two lines.
        val updatedData = MutableLiveData<List<Vehicle>>()
        `when`(dao.getAll()).thenReturn(updatedData)

        //simulate there is nothing in database, and we request data from internet.
        data.postValue(null)
        Mockito.verify(service).getVehicles()
        Mockito.verify(dao).deleteAll()
        Mockito.verify(dao).insert(list)

        //so database is updated and in observers, we notify it.
        updatedData.postValue(list)
        Mockito.verify(observer).onChanged(Resource.success(list))
    }


    private fun createVehicle(bearing: Int, type: String = "ECO"): Vehicle =
        Vehicle(type, 0.0, 0.0, bearing, "")
}