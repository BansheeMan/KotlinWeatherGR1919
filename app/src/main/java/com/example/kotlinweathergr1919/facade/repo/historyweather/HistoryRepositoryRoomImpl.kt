package com.example.kotlinweathergr1919.facade.repo.historyweather

import com.example.kotlinweathergr1919.MyApp
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.facade.repo.details.DetailsRepository
import com.example.kotlinweathergr1919.utils.convertHistoryEntityToWeather
import com.example.kotlinweathergr1919.utils.convertWeatherToEntity
import com.example.kotlinweathergr1919.view_viewmodel.details.OnServersResponse
import com.example.kotlinweathergr1919.view_viewmodel.details.ResponseState
import com.example.kotlinweathergr1919.view_viewmodel.historylist.HistoryWeatherListViewModel

class HistoryRepositoryRoomImpl : HistoryRepositoryAdd, HistoryRepositoryAll, DetailsRepository {
    override fun getAllWeatherHistory(callback: HistoryWeatherListViewModel.CallbackForAll) {
        Thread {
            callback.onResponse(
                convertHistoryEntityToWeather(
                    MyApp.getHistoryDao().getAll()
                )
            )
        }.start()
    }

    override fun getWeatherDetails(city: City, onServersResponse: OnServersResponse) {
        Thread {
            val list =
                convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryCity(city.name))
            if (list.isEmpty()) {
                onServersResponse.onResponse(ResponseState.Errors())
            } else {
                onServersResponse.onResponse(ResponseState.Success(list.last()))
            }
        }.start()
    }

    override fun addWeather(weather: Weather) {
        Thread { MyApp.getHistoryDao().insert(convertWeatherToEntity(weather)) }.start()
    }
}