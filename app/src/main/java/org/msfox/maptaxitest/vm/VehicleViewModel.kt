package org.msfox.maptaxitest.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.msfox.maptaxitest.model.Vehicle
import org.msfox.maptaxitest.repository.Resource
import org.msfox.maptaxitest.repository.VehicleRepository

/**
 * Created by mohsen on 06,July,2020
 */
abstract class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {

    abstract fun getVehicles(): LiveData<Resource<List<Vehicle>>>

    protected fun getVehicles(offlineMode: Boolean) = vehicleRepository.loadVehicles(offlineMode)
}