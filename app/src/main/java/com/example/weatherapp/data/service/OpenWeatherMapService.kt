package com.example.weatherapp.data.service

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.service.dto.WeatherResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherMapService {
    @GET("weather?appid=${BuildConfig.API_KEY}&units=metric")

    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<WeatherResponse>

}
    fun OpenWeatherMapService(): OpenWeatherMapService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }