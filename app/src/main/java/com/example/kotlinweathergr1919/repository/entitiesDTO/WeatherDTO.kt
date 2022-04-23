package com.example.kotlinweathergr1919.repository.entitiesDTO


import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: Fact,
    @SerializedName("forecast")
    val forecast: Forecast,
    @SerializedName("info")
    val info: Info,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String
)