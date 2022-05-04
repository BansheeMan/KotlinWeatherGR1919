package com.example.kotlinweathergr1919.facade.repo.details.retrofit

import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.LAT
import com.example.kotlinweathergr1919.utils.LON
import com.example.kotlinweathergr1919.utils.YANDEX_API_KEY
import com.example.kotlinweathergr1919.utils.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)                     //ONLY ENDPOINT !!! OOOOONLY...
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(LAT) lat: Double,
        @Query(LON) lon: Double
    ): Call<WeatherDTO>
}