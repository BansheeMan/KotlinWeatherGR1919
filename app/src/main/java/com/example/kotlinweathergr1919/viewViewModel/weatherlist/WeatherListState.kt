package com.example.kotlinweathergr1919.viewViewModel.weatherlist

import com.example.kotlinweathergr1919.facade.entities.Weather

sealed class WeatherListState {
    data class Success(val weatherData: List<Weather>) : WeatherListState()
    data class Error(val error: Throwable) : WeatherListState()
    object Loading : WeatherListState()
}