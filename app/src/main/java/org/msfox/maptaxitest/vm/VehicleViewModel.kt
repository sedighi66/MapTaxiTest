package org.msfox.maptaxitest.vm

import androidx.lifecycle.ViewModel
import org.msfox.maptaxitest.repository.VehicleRepository
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
abstract class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {

    fun getVehicles(offlineMode: Boolean) = vehicleRepository.loadVehicles(offlineMode)
}