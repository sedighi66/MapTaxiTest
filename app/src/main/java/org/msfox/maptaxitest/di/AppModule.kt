package org.msfox.maptaxitest.di

/**
 * Created by mohsen on 04,July,2020
 */

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.msfox.maptaxitest.api.SnappService
import org.msfox.maptaxitest.db.AppDb
import org.msfox.maptaxitest.db.VehicleDao
import org.msfox.maptaxitest.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideSnappService(): SnappService {
        return Retrofit.Builder()
            .baseUrl("https://snapp.ir/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(SnappService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "appdb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideVehicleDao(db: AppDb): VehicleDao {
        return db.vehicleDao()
    }

}
