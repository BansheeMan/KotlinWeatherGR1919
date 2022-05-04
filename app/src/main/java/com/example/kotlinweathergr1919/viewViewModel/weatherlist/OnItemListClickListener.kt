package com.example.kotlinweathergr1919.viewViewModel.weatherlist

import com.example.kotlinweathergr1919.facade.entities.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)
}