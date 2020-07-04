package org.msfox.maptaxitest.db


import androidx.room.Database
import androidx.room.RoomDatabase
import org.msfox.maptaxitest.model.Vehicle

/**
 * Created by mohsen on 04,July,2020
 */
@Database(entities = [Vehicle::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao
}
