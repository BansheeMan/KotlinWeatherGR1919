package com.example.kotlinweathergr1919.view.details

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {

    private val message = Intent(KEY_WAVE_SERVICE_BROADCAST)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)

            val urlText = "$SERVER_ANDREY$YANDEX_ENDPOINT$LAT$lat&$LON$lon"
            val uri = URL(urlText)
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(YANDEX_WEATHER_API, BuildConfig.WEATHER_API_KEY)
                }
            try {
                val responseCode = urlConnection.responseCode
                val responseCodeAndMessage =
                    "${urlConnection.responseCode} ${urlConnection.responseMessage}"
                val clientOrServerSide = 400..599

                when (responseCode) {
                    in clientOrServerSide -> {
                        putErrorClientOrServer(responseCode, responseCodeAndMessage)
                    }
                    else -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        putSuccessWeather(weatherDTO)
                    }
                }
            } catch (e: JsonSyntaxException) { //как правило закончились 50 бесплатных суточных запросов
                putErrorRanOutOfRequests()
            } catch (e: SocketTimeoutException) { //"Timeout is over..."
                putOtherErrors(e.message.toString())
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    private fun putSuccessWeather(weatherDTO: WeatherDTO) {
        message.putExtra(DETAILS_LOAD_RESULT_EXTRA, KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun putErrorClientOrServer(responseCode: Int, responseCodeAndMessage: String) {
        message.putExtra(DETAILS_LOAD_RESULT_EXTRA, KEY_BUNDLE_SERVICE_BROADCAST_ERROR)
        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR, responseCode)
        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR, responseCodeAndMessage)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun putErrorRanOutOfRequests() {
        message.putExtra(
            DETAILS_LOAD_RESULT_EXTRA,
            KEY_BUNDLE_SERVICE_BROADCAST_ERROR_RAN_OUT_OF_REQUEST
        )
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun putOtherErrors(responseCodeAndMessage: String) {
        message.putExtra(DETAILS_LOAD_RESULT_EXTRA, KEY_BUNDLE_SERVICE_BROADCAST_ERROR_OTHER)
        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR, responseCodeAndMessage)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }
}