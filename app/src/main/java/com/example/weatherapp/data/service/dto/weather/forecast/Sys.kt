package com.example.weatherapp.data.service.dto.weather.forecast


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)