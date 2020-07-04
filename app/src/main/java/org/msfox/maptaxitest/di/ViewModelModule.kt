package org.msfox.maptaxitest.di

/**
 * Created by mohsen on 04,July,2020
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.msfox.maptaxitest.di.ViewModelKey

@Suppress("unused")
@Module
abstract class ViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(UserViewModel::class)
//    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SearchViewModel::class)
//    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(RepoViewModel::class)
//    abstract fun bindRepoViewModel(repoViewModel: RepoViewModel): ViewModel
//
//    @Binds
//    abstract fun bindViewModelFactory(factory: GithubViewModelFactory): ViewModelProvider.Factory
}
