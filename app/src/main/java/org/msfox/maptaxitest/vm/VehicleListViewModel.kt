package org.msfox.maptaxitest.vm

import androidx.lifecycle.LiveData
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.VehicleRepository
import org.msfox.maptaxitest.utils.OpenForTesting
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
@OpenForTesting
class VehicleListViewModel @Inject constructor(private val vehicleRepository: VehicleRepository)
    : VehicleViewModel(vehicleRepository){

    override fun getVehicles(): LiveData<Resource<List<Vehicle>>> = getVehicles(true)
}