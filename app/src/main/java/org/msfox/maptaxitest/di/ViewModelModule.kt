package org.msfox.maptaxitest.di

/**
 * Created by mohsen on 04,July,2020
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.msfox.maptaxitest.vm.AppViewModelFactory
import org.msfox.maptaxitest.vm.VehicleListViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VehicleListViewModel::class)
    abstract fun bindVehicleListViewModel(vehicleListViewModel: VehicleListViewModel): ViewModel
}
