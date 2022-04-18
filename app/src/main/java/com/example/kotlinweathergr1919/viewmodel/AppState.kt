package com.example.kotlinweathergr1919.viewmodel

import com.example.kotlinweathergr1919.repository.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}