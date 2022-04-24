package com.example.kotlinweathergr1919.viewmodel

import com.example.kotlinweathergr1919.R.string.forbidden_try_tomorrow
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO

sealed class ResponseState {
    data class Success(val weatherDTO: WeatherDTO) : ResponseState()
    data class Errors(
        val responseCode: Int = 666,
        val responseCodeAndMessage: String = "Unknown Error"
    ) : ResponseState()

    data class ErrorRanOutOfRequests(   //ошибка если истекло 50 запросов суточных
        val responseCode: Int = 403,
        val responseCodeAndMessage: Int = forbidden_try_tomorrow
    ) : ResponseState()


}