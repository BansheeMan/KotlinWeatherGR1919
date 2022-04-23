package com.example.kotlinweathergr1919.repository

import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO

interface OnServersResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}