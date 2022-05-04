package com.example.kotlinweathergr1919.facade.repo.details

import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.*
import com.example.kotlinweathergr1919.viewViewModel.details.OnServersResponse
import com.example.kotlinweathergr1919.viewViewModel.details.ResponseState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import java.io.IOException

class DetailsRepositoryOkHttpEnqueueImpl : DetailsRepository {
    override fun getWeatherDetails(city: City, onServersResponse: OnServersResponse) {
        val client = OkHttpClient()
        val builder = Request.Builder().apply {
            addHeader(YANDEX_WEATHER_API, BuildConfig.WEATHER_API_KEY)
            url("$SERVER_ANDREY$YANDEX_ENDPOINT$LAT${city.lat}&$LON${city.lon}")
        }
        val request = builder.build()
        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onServersResponse.onResponse(ResponseState.Errors(responseMessage = ("$e ${e.printStackTrace()}")))
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val weatherDTO: WeatherDTO =
                            Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
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
            }
        }
        val call = client.newCall(request)
        call.enqueue(callback)
    }
}