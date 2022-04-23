package com.example.kotlinweathergr1919.repository

import com.example.kotlinweathergr1919.repository.entities.Weather
import com.example.kotlinweathergr1919.repository.entities.getRussianCities
import com.example.kotlinweathergr1919.repository.entities.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWorldWeatherFromLocalStorage(): List<Weather> = getWorldCities()
    override fun getRussianWeatherFromLocalStorage(): List<Weather> = getRussianCities()

}