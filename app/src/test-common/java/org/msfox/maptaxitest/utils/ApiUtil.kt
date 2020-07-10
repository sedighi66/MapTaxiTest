package org.msfox.maptaxitest.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.msfox.maptaxitest.api.ApiResponse
import retrofit2.Response

/**
 * Created by mohsen on 05,July,2020
 */
object ApiUtil {
    fun <T : Any> successCall(data: T) = createCall(Response.success(data))

     fun <T : Any> createCall(response: Response<T>) =
        MutableLiveData<ApiResponse<T>>().apply {
        value = ApiResponse.create(response)
    } as LiveData<ApiResponse<T>>
}