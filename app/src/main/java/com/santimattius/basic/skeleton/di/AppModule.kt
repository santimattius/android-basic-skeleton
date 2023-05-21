package com.santimattius.basic.skeleton.di

import com.santimattius.basic.skeleton.core.networking.RetrofitServiceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofitCreator(): RetrofitServiceCreator {
        return RetrofitServiceCreator(baseUrl = "https://www.example.com/api")
    }
}