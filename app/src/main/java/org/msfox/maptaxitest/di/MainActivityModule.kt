package org.msfox.maptaxitest.di

/**
 * Created by mohsen on 04,July,2020
 */


import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.msfox.maptaxitest.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
