package com.example.weatherapp.di

import com.example.weatherapp.data.local.WeatherLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideLocalRepository(
    ): WeatherLocalRepository {
        return WeatherLocalRepository()
    }
}