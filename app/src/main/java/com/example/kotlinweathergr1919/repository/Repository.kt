package com.example.kotlinweathergr1919.repository

import com.example.kotlinweathergr1919.repository.entities.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage():List<Weather>
    fun getRussianWeatherFromLocalStorage():List<Weather>
}