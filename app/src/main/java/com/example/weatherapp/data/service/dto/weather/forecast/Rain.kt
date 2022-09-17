package com.example.weatherapp.data.service.dto.weather.forecast


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)