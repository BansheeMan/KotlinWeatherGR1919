package com.example.kotlinweathergr1919.utils

import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.facade.entities.getDefaultCity
import com.example.kotlinweathergr1919.facade.entitiesDTO.Fact
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO

const val KEY_BUNDLE_WEATHER = "KEY_BUNDLE_WEATHER"
const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val SERVER_ANDREY = "http://212.86.114.27/"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"      //спциально оставил, чтобы было быстрее копипастить и переключаться
const val YANDEX_ENDPOINT = "v2/informers?"
const val LAT = "lat"
const val LON = "lon"

const val KEY_BUNDLE_LON = "KEY_BUNDLE_LON"
const val KEY_BUNDLE_LAT = "KEY_BUNDLE_LAT"

const val KEY_WAVE_SERVICE_BROADCAST = "KEY_WAVE_SERVICE_BROADCAST"
const val DETAILS_LOAD_RESULT_EXTRA = "DETAILS_LOAD_RESULT_EXTRA"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "KEY_BUNDLE_SERVICE_BROADCAST_WEATHER"
const val KEY_BUNDLE_SERVICE_BROADCAST_ERROR = "KEY_BUNDLE_SERVICE_BROADCAST_ERROR"
const val KEY_BUNDLE_SERVICE_BROADCAST_ERROR_RAN_OUT_OF_REQUEST =
    "KEY_BUNDLE_SERVICE_BROADCAST_ERROR_RAN_OUT_OF_REQUEST"
const val KEY_BUNDLE_SERVICE_BROADCAST_ERROR_OTHER = "KEY_BUNDLE_SERVICE_BROADCAST_ERROR_OTHER"

fun convertDTOtoModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike))
}