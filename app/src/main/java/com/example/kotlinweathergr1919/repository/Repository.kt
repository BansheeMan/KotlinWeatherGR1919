package com.example.kotlinweathergr1919.repository

interface Repository {
    fun getWeatherFromServer():Weather
    fun getWorldWeatherFromLocalStorage():List<Weather>
    fun getRussianWeatherFromLocalStorage():List<Weather>
}