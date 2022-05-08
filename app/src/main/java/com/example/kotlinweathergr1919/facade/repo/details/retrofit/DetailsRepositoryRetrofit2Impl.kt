package com.example.kotlinweathergr1919.facade.repo.details.retrofit

import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.MyApp.Companion.weatherAPI
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.facade.repo.details.DetailsRepository
import com.example.kotlinweathergr1919.utils.convertDTOtoModel
import com.example.kotlinweathergr1919.view_viewmodel.details.OnServersResponse
import com.example.kotlinweathergr1919.view_viewmodel.details.ResponseState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, onServersResponse: OnServersResponse) {
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