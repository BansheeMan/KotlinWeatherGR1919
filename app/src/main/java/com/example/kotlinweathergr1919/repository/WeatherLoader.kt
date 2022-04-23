package com.example.kotlinweathergr1919.repository

import android.os.Handler
import android.os.Looper
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onServersResponse: OnServersResponse) {

    fun loadWeather(lat: Double, lon: Double) {

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
            connectTimeout = 1000
            readTimeout = 1000
            addRequestProperty("X-Yandex-API-Key", "803e8ac9-67ac-4227-ad52-74d177745393")
        }

        Thread {
            try {
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    urlConnection.responseCode
                    onServersResponse.onResponse(weatherDTO)
                }
            } finally {
                urlConnection.disconnect()
            }


        }.start()
    }
}