package com.example.kotlinweathergr1919.repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorage() = Weather()
}