package com.example.kotlinweathergr1919.repository

import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.viewmodel.ResponseState

interface OnServersResponse {
    fun onResponse(responseState: ResponseState)
}