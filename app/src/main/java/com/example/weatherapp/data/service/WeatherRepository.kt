package com.example.weatherapp.data.service

import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.service.dto.location.LocationResponse
import com.example.weatherapp.data.service.dto.weather.WeatherResponse
import com.example.weatherapp.data.service.dto.weather.forecast.ForecastResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepository @Inject constructor(
    private val weatherService: OpenWeatherService,
) {
    data class SimplifiedForecast(
        val time: LocalTime,
        val date: LocalDate,
        val conditions: String,
        val temp: Int
    )

    private fun simplifyForecastResponse(response: ForecastResponse): List<SimplifiedForecast> {
        return response.list.map { forecast ->
            with(
                LocalDateTime.parse(
                    forecast.dtTxt,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
            ) {
                SimplifiedForecast(
                    time = this.toLocalTime(),
                    date = this.toLocalDate(),
                    conditions = forecast.weather.first().main,
                    temp = forecast.main.temp.roundToInt()
                )
            }
        }
    }


//    private val weather = MutableStateFlow<WeatherResponse?>(null)

//    fun getWeather(): Flow<WeatherResponse?> = weather

    fun currentWeather(appContext: Context): Flow<WeatherResponse?> {
        return locationFlow(appContext).map {
            weatherService.getCurrentWeather(it.latitude, it.longitude, BuildConfig.API_KEY).body()
        }
    }

    fun currentLocation(appContext: Context): Flow<LocationResponse?> {
        return locationFlow(appContext).map {
            weatherService.getCurrentLocation(it.latitude, it.longitude, BuildConfig.API_KEY).body()
        }
    }

    fun weatherForecast(appContext: Context): Flow<List<SimplifiedForecast>> {
        return locationFlow(appContext).map {
            weatherService.getWeatherForecast(it.latitude, it.longitude, BuildConfig.API_KEY).body()
        }
            .filterNotNull().map {
                simplifyForecastResponse(it)
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