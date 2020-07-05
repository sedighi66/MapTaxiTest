package org.msfox.maptaxitest.repository

import androidx.lifecycle.LiveData
import org.msfox.maptaxitest.AppCoroutineDispatchers
import org.msfox.maptaxitest.api.ApiResponse
import org.msfox.maptaxitest.api.SnappService
import org.msfox.maptaxitest.db.VehicleDao
import org.msfox.maptaxitest.model.Document
import org.msfox.maptaxitest.model.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Vehicle Repository
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
class VehicleRepository @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val vehicleDao: VehicleDao,
    private val snappService: SnappService
) {

    fun loadVehicles(offLineMode: Boolean): LiveData<Resource<List<Vehicle>>> {
        return object: NetworkBoundResource<List<Vehicle>, Document>(appCoroutineDispatchers){
            override fun saveCallResult(item: Document) {
                vehicleDao.deleteAll()
                vehicleDao.insert(item.vehicles)
            }

            override fun shouldFetch(data: List<Vehicle>?): Boolean = !offLineMode


            override fun loadFromDb(): LiveData<List<Vehicle>> = vehicleDao.getAll()


            override fun createCall(): LiveData<ApiResponse<Document>> = snappService.getVehicles()

        }.asLiveData()
    }
}
