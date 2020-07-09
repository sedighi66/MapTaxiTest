package org.msfox.maptaxitest.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.mockito.Mockito.*
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.Status
import org.msfox.maptaxitest.repository.VehicleRepository
import org.msfox.maptaxitest.utils.Creators.createVehicle
import org.msfox.maptaxitest.utils.MainCoroutineRule

/**
 * Created by mohsen on 07,July,2020
 */
@ExperimentalCoroutinesApi
class VehicleListViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepo: VehicleRepository
    private lateinit var viewModel: VehicleListViewModel

    @Before
    fun init() {
        mockRepo = mock(VehicleRepository::class.java)
        viewModel = VehicleListViewModel(mockRepo)
    }


    @Test
    fun `get vehicles when called should call load vehicles method in repo in offline mode`() {
        val list = mutableListOf(createVehicle(1))
        val data = MutableLiveData<Resource<List<Vehicle>>>(Resource(Status.SUCCESS, list, null))
        //TODO: false should be changed to true after finishing and testing vehicle list vm.
        `when`(mockRepo.loadVehicles(false)).thenReturn(data)

        val vehicles = viewModel.getVehicles()
        verify(mockRepo).loadVehicles(false)
        Assert.assertEquals(data, vehicles)
    }


}