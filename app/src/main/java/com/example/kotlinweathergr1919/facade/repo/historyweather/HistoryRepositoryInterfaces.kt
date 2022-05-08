package com.example.kotlinweathergr1919.facade.repo.historyweather

import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.view_viewmodel.historylist.HistoryWeatherListViewModel

interface HistoryRepositoryAdd {
    fun addWeather(weather: Weather)
}

interface HistoryRepositoryAll {
    fun getAllWeatherHistory(callback: HistoryWeatherListViewModel.CallbackForAll)
}






