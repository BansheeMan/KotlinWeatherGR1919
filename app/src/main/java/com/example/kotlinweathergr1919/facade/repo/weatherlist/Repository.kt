package com.example.kotlinweathergr1919.facade.repo.weatherlist

import com.example.kotlinweathergr1919.facade.entities.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage():List<Weather>
    fun getRussianWeatherFromLocalStorage():List<Weather>
}