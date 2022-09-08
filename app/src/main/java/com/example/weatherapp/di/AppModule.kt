package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.local.WeatherLocalRepository
import com.example.weatherapp.data.service.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideLocalRepository(): WeatherLocalRepository {
        return WeatherLocalRepository()
    }

    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepository()
    }
}