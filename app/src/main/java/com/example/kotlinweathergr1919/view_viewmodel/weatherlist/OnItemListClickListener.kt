package com.example.kotlinweathergr1919.view_viewmodel.weatherlist

import com.example.kotlinweathergr1919.facade.entities.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}