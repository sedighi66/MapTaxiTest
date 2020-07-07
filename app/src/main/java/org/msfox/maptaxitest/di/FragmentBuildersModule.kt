package org.msfox.maptaxitest.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.msfox.maptaxitest.ui.VehicleListFragment

/**
 * Created by mohsen on 04,July,2020
 */

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeVehicleListFragment(): VehicleListFragment
}
