package org.msfox.maptaxitest.vm

import androidx.lifecycle.LiveData
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.VehicleRepository
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
class VehicleListViewModel @Inject constructor(private val vehicleRepository: VehicleRepository)
    : VehicleViewModel(vehicleRepository){

    //TODO: after finishing of implementing all the vehicle-list, offlineMode should be true.
    override fun getVehicles(): LiveData<Resource<List<Vehicle>>> = getVehicles(false)
}