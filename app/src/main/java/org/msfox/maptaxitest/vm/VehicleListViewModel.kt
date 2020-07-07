package org.msfox.maptaxitest.vm

import org.msfox.maptaxitest.repository.VehicleRepository
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
class VehicleListViewModel @Inject constructor(private val vehicleRepository: VehicleRepository)
    : VehicleViewModel(vehicleRepository){


}