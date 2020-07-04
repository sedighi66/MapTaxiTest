package org.msfox.maptaxitest.di

/**
 * Created by mohsen on 04,July,2020
 */

import dagger.Module
import dagger.Provides
import org.msfox.maptaxitest.api.SnappService
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
          //  .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(SnappService::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideDb(app: Application): SnappService {
//        return Room
//            .databaseBuilder(app, Snapp::class.java, "github.db")
//            .fallbackToDestructiveMigration()
//            .build()
//    }

//    @Singleton
//    @Provides
//    fun provideUserDao(db: GithubDb): UserDao {
//        return db.userDao()
//    }
//
//    @Singleton
//    @Provides
//    fun provideRepoDao(db: GithubDb): RepoDao {
//        return db.repoDao()
//    }
}
