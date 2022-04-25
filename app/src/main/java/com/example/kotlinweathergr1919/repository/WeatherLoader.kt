package com.example.kotlinweathergr1919.repository

import android.os.Handler
import android.os.Looper
import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.KEY_WEATHER_API
import com.example.kotlinweathergr1919.viewmodel.ResponseState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class WeatherLoader(private val onServersResponse: OnServersResponse) {

    fun loadWeather(lat: Double, lon: Double) {
        //val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val urlText = "http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
        val uri = URL(urlText)
        //val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
        val urlConnection: HttpURLConnection = (uri.openConnection() as HttpURLConnection).apply {
            connectTimeout = 1000
            readTimeout = 1000
            addRequestProperty(KEY_WEATHER_API, BuildConfig.WEATHER_API_KEY)
        }

        Thread {
            try {
                val responseCode = urlConnection.responseCode
                val responseCodeAndMessage =
                    "${urlConnection.responseCode} ${urlConnection.responseMessage}"
                val clientsOrServerSide = 400..599
                when (responseCode) {
                    in clientsOrServerSide -> {
                        Handler(Looper.getMainLooper()).post {
                            onServersResponse.onResponse(
                                ResponseState.Errors(
                                    responseCode,
                                    responseCodeAndMessage
                                )
                            )
                        }
                    }
                    else -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        Handler(Looper.getMainLooper()).post {
                            onServersResponse.onResponse(ResponseState.Success(weatherDTO))
                        }
                    }
                }
            } catch (e: JsonSyntaxException) {
                Handler(Looper.getMainLooper()).post {
                    onServersResponse.onResponse(ResponseState.ErrorRanOutOfRequests()) //как правило закончились 50 бесплатных суточных запросов
                }
            } catch (e: SocketTimeoutException) {
                Handler(Looper.getMainLooper()).post {
                    onServersResponse.onResponse(
                        ResponseState.Errors(
                            responseCodeAndMessage = "Timeout is over..."
                        )
                    )
                }
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}