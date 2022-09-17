package com.example.weatherapp.data.service

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.service.dto.location.LocationResponse
import com.example.weatherapp.data.service.dto.weather.WeatherResponse
import com.example.weatherapp.data.service.dto.weather.forecast.ForecastResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("data/2.5/weather?units=metric&lang=ru")

    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<WeatherResponse>


    @GET("geo/1.0/reverse?units=metric")
    suspend fun getCurrentLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<LocationResponse>

    @GET("data/2.5/forecast?units=metric")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<ForecastResponse>
}