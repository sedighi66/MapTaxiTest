package org.msfox.maptaxitest.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import org.msfox.maptaxitest.MapTaxiTestApp
import javax.inject.Singleton

/**
 * Created by mohsen on 04,July,2020
 */

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mapTaxiTestApp: MapTaxiTestApp)
}
