package com.example.kotlinweathergr1919.facade.repo.weatherlist

import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.facade.entities.getRussianCities
import com.example.kotlinweathergr1919.facade.entities.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWorldWeatherFromLocalStorage(): List<Weather> = getWorldCities()
    override fun getRussianWeatherFromLocalStorage(): List<Weather> = getRussianCities()

}