package com.example.weatherapp.data.service.dto.weather.forecast


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)