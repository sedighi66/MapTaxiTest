package org.msfox.maptaxitest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.msfox.maptaxitest.model.Vehicle

/**
 * Created by mohsen on 04,July,2020
 */
@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicles: List<Vehicle>)

    @Query("select * from vehicle_table")
    fun getAll(): LiveData<List<Vehicle>>

    @Query("delete from vehicle_table")
    suspend fun deleteAll()

}