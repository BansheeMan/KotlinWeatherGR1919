package com.example.kotlinweathergr1919.repository.entitiesDTO


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Forecast(
    @SerializedName("date")
    val date: String,
    @SerializedName("date_ts")
    val dateTs: Int,
    @SerializedName("moon_code")
    val moonCode: Int,
    @SerializedName("moon_text")
    val moonText: String,
    @SerializedName("parts")
    val parts: List<Part>,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("week")
    val week: Int
) : Parcelable