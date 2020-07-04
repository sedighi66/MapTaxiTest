package org.msfox.maptaxitest.api

import org.msfox.maptaxitest.model.Vehicle
import retrofit2.http.GET

/**
 * Created by mohsen on 04,July,2020
 */
interface SnappService {

    @GET("assets/test/document.json")
    fun getVehicles(): List<Vehicle>
}