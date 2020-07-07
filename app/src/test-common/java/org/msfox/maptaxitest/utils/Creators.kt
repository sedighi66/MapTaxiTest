package org.msfox.maptaxitest.utils

import org.msfox.maptaxitest.model.Vehicle

/**
 * Created by mohsen on 07,July,2020
 */
object Creators {

     fun createVehicle(bearing: Int, type: String = "ECO"): Vehicle =
        Vehicle(type, 0.0, 0.0, bearing, "")
}