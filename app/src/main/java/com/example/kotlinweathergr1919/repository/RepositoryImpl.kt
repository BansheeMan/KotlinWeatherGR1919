package com.example.kotlinweathergr1919.repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWorldWeatherFromLocalStorage(): List<Weather> = getWorldCities()
    override fun getRussianWeatherFromLocalStorage(): List<Weather> = getRussianCities()

}