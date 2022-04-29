package com.example.kotlinweathergr1919.repository.entitiesDTO


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fact(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("daytime")
    val daytime: String,
    @SerializedName("feels_like")
    val feelsLike: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("obs_time")
    val obsTime: Int,
    @SerializedName("polar")
    val polar: Boolean,
    @SerializedName("pressure_mm")
    val pressureMm: Int,
    @SerializedName("pressure_pa")
    val pressurePa: Int,
    @SerializedName("season")
    val season: String,
    @SerializedName("temp")
    val temperature: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
) : Parcelable