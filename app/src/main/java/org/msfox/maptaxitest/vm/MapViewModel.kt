package org.msfox.maptaxitest.vm

import androidx.lifecycle.LiveData
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.VehicleRepository
import javax.inject.Inject

/**
 * Created by mohsen on 13,July,2020
 */
class MapViewModel @Inject constructor(private val repository: VehicleRepository):
    VehicleViewModel(repository) {

    override fun getVehicles(): LiveData<Resource<List<Vehicle>>> =
        getVehicles(false)

}