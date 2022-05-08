package com.example.kotlinweathergr1919.facade.repo.details

import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.view_viewmodel.details.OnServersResponse

interface DetailsRepository {
    fun getWeatherDetails(city: City, onServersResponse: OnServersResponse)
}