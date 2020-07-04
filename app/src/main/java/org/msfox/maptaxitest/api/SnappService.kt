package org.msfox.maptaxitest.api

import androidx.lifecycle.LiveData
import org.msfox.maptaxitest.model.Document
import retrofit2.http.GET

/**
 * Created by mohsen on 04,July,2020
 */
interface SnappService {

    @GET("assets/test/document.json")
    fun getVehicles(): LiveData<ApiResponse<Document>>
}