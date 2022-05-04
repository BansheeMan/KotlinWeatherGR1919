package com.example.kotlinweathergr1919.facade.repo.details

import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.*
import com.example.kotlinweathergr1919.viewViewModel.details.OnServersResponse
import com.example.kotlinweathergr1919.viewViewModel.details.ResponseState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient
import okhttp3.Request

class DetailsRepositoryOkHttpExecuteImpl : DetailsRepository {
    override fun getWeatherDetails(city: City, onServersResponse: OnServersResponse) {
        val client = OkHttpClient()
        val builder = Request.Builder().apply {
            addHeader(YANDEX_WEATHER_API, BuildConfig.WEATHER_API_KEY)
            url("$SERVER_ANDREY$YANDEX_ENDPOINT$LAT${city.lat}&$LON${city.lon}")
        }
        val request = builder.build()
        val call = client.newCall(request)
        Thread {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val serversResponse = response.body()!!.string()
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(serversResponse, WeatherDTO::class.java)
                    val weather = convertDTOtoModel(weatherDTO)
                    weather.city = city
                    onServersResponse.onResponse(ResponseState.Success(weather))
                } else {
                    onServersResponse.onResponse(
                        ResponseState.Errors(
                            response.code(),
                            response.message()
                        )
                    )
                }
            } catch (e: JsonSyntaxException) {    //истекло 50 запросов
                onServersResponse.onResponse(ResponseState.ErrorRanOutOfRequests())
            }
        }.start()

    }
}