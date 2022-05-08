package com.example.kotlinweathergr1919.view_viewmodel.historylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.facade.repo.historyweather.HistoryRepositoryRoomImpl
import com.example.kotlinweathergr1919.view_viewmodel.weatherlist.WeatherListState

class HistoryWeatherListViewModel(
    private val liveData: MutableLiveData<WeatherListState> = MutableLiveData(),
    private val repository: HistoryRepositoryRoomImpl = HistoryRepositoryRoomImpl()
) : ViewModel() {

    fun getData(): LiveData<WeatherListState> {
        return liveData
    }

    fun getAll() {
        liveData.postValue(WeatherListState.Loading)
        repository.getAllWeatherHistory(object: CallbackForAll {
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(WeatherListState.Success(listWeather))
            }
        } )
    }

    interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)
    }
}