package com.example.kotlinweathergr1919.view.weatherlist

import com.example.kotlinweathergr1919.repository.entities.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}