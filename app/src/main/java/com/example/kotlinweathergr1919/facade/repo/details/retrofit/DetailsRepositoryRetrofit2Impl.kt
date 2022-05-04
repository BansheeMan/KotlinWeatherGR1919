package com.example.kotlinweathergr1919.facade.repo.details.retrofit

import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.facade.repo.details.DetailsRepository
import com.example.kotlinweathergr1919.utils.YANDEX_DOMAIN
import com.example.kotlinweathergr1919.utils.convertDTOtoModel
import com.example.kotlinweathergr1919.viewViewModel.details.OnServersResponse
import com.example.kotlinweathergr1919.viewViewModel.details.ResponseState
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, onServersResponse: OnServersResponse) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)
        // val response = weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).execute() можно так     СИНХРОННО!!!
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object :
                Callback<WeatherDTO> {                                                                 // АСИНХРОННО!!!
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = convertDTOtoModel(it)
                            weather.city = city
                            onServersResponse.onResponse(ResponseState.Success(weather))
                        }
                    } else {
                        onServersResponse.onResponse(
                            ResponseState.Errors(
                                response.code(), response.message()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    onServersResponse.onResponse(ResponseState.Errors(responseMessage = ("$t ${t.printStackTrace()}")))
                }
            })
    }
}