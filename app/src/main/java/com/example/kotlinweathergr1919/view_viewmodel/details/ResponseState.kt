package com.example.kotlinweathergr1919.view_viewmodel.details

import com.example.kotlinweathergr1919.R.string.forbidden_try_tomorrow
import com.example.kotlinweathergr1919.facade.entities.Weather

sealed class ResponseState {
    data class Success(val weather: Weather) : ResponseState()
    object Loading : ResponseState()
    data class Errors(
        val responseCode: Int = 0,
        val responseMessage: String = "Unknown Error"
    ) : ResponseState()

    data class ErrorRanOutOfRequests(   //ошибка если истекло 50 запросов суточных
        val responseCode: Int = 403,
        val responseMessage: Int = forbidden_try_tomorrow
    ) : ResponseState()


}