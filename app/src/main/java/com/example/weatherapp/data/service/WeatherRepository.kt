package com.example.weatherapp.data.service

import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.service.dto.location.LocationResponse
import com.example.weatherapp.data.service.dto.weather.WeatherResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService : OpenWeatherMapWeatherService,
    private val locationService : OpenWeatherMapLocationService
) {



    private val weather = MutableStateFlow<WeatherResponse?>(null)

    fun getWeather(): Flow<WeatherResponse?> = weather

    fun currentLocationWeather(appContext: Context): Flow<WeatherResponse?> {
        return locationFlow(appContext).map {
            weatherService.getCurrentWeather(it.latitude, it.longitude, BuildConfig.API_KEY).body()
        }
    }

    fun currentLocation(appContext: Context): Flow<LocationResponse?> {
        return locationFlow(appContext).map {
            locationService.getCurrentLocation(it.latitude, it.longitude, BuildConfig.API_KEY).body()
        }
    }


    private fun locationFlow(appContext: Context) = channelFlow<Location> {
        val client = LocationServices.getFusedLocationProviderClient(appContext)
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation)
            }
        }
        val request = LocationRequest.create()
            .setInterval(10_000)
            .setFastestInterval(5_000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setSmallestDisplacement(170f)
        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}