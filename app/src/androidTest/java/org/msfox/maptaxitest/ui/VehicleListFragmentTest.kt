package org.msfox.maptaxitest.ui

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.binding.FragmentBindingAdapters
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.utils.*
import org.msfox.maptaxitest.vm.VehicleListViewModel
import org.msfox.maptaxitest.vm.VehicleViewModel

/**
 * Created by mohsen on 08,July,2020
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class VehicleListFragmentTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val vehicleLiveData = MutableLiveData<Resource<List<Vehicle>>>()
    private lateinit var viewModel: VehicleViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters

    @Before
    fun init() {
        viewModel = Mockito.mock(VehicleListViewModel::class.java)
        mockBindingAdapter = Mockito.mock(FragmentBindingAdapters::class.java)
        Mockito.`when`(viewModel.getVehicles()).thenReturn(vehicleLiveData)

        val scenario = launchFragmentInContainer()
        {
            VehicleListFragment().apply {
                appCoroutineDispatchers = coroutineRule.getAppCoroutineDispatchers()
                viewModelFactory = ViewModelUtil.createFor(viewModel)
                dataBindingComponent = object : DataBindingComponent {
                    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                        return mockBindingAdapter
                    }
                }
            }
        }
    }


    @Test
    fun test_isRecyclerViewInFragment() {
        vehicleLiveData.postValue(Resource.loading(null))
        onView(withId(R.id.vehicle_list)).check(matches(isDisplayed()))
    }

}