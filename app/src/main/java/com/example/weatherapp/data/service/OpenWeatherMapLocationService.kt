package com.example.weatherapp.data.service

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.service.dto.location.LocationResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapLocationService {

    @GET("reverse?appid=${BuildConfig.API_KEY}")

    suspend fun getCurrentLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Response<LocationResponse>

}