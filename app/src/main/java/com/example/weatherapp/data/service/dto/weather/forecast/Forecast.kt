package com.example.weatherapp.data.service.dto.weather.forecast


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("dt_txt")
    val dtTxt: String,
    @SerializedName("main")
    val main: Main,
    @SerializedName("pop")
    val pop: Double,
    @SerializedName("rain")
    val rain: Rain,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)