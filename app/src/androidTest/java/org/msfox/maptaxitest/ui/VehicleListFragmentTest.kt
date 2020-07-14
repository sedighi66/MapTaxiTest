package org.msfox.maptaxitest.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.adapter.DataBoundViewHolder
import org.msfox.maptaxitest.binding.FragmentBindingAdapters
import org.msfox.maptaxitest.databinding.VehicleItemBinding
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.Status
import org.msfox.maptaxitest.utils.Creators.createVehicle
import org.msfox.maptaxitest.utils.MainCoroutineRule
import org.msfox.maptaxitest.utils.ViewModelUtil
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
    fun isRecyclerViewInFragment() {
        vehicleLiveData.postValue(Resource.loading(null))
        onView(withId(R.id.vehicle_list)).check(matches(isDisplayed()))
    }


    @Test
    fun isItemNumber1InRecyclerView_vehicleTypeTextIsCorrect() {
        val list = mutableListOf(createVehicle(1))
        vehicleLiveData.postValue(Resource(Status.SUCCESS, list, null))

        val itemElementText = getString(R.string.vehicle_type, list[0].type)

        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    /**
     * when we need scroll
     */
    @Test
    fun isItemNumber15InRecyclerView_vehicleTypeTextIsCorrect() {
        // itemIndex should be in the list
        val itemIndex = 15
        val list = mutableListOf<Vehicle>()
        for(i in 0..20)
            if(i == itemIndex) list.add(createVehicle(i, "PLUS")) else list.add(createVehicle(i))

        vehicleLiveData.postValue(Resource(Status.SUCCESS, list, null))
        val itemElementText = getString(R.string.vehicle_type, list[itemIndex].type)

        onView(withId(R.id.vehicle_list))
            .perform(RecyclerViewActions
                    //click() is a fake action
                .actionOnItemAtPosition<DataBoundViewHolder<VehicleItemBinding>>(itemIndex, click()))

        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    private fun getString(@StringRes id: Int, vararg args: Any): String {
        return ApplicationProvider.getApplicationContext<Context>().getString(id, *args)
    }
}














