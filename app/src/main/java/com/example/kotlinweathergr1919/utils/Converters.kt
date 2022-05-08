package com.example.kotlinweathergr1919.utils

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.kotlinweathergr1919.domain.room.HistoryEntity
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.facade.entities.getDefaultCity
import com.example.kotlinweathergr1919.facade.entitiesDTO.Fact
import com.example.kotlinweathergr1919.facade.entitiesDTO.WeatherDTO

fun convertDTOtoModel(weatherDTO: WeatherDTO): Weather {
    val fact: Fact = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, it.lat, it.lon), it.temperature, it.feelsLike, it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.city.lat, weather.city.lon, weather.temperature,weather.feelsLike, weather.icon)
}

internal fun ImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .build()
    imageLoader.enqueue(request)
}