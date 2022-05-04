package com.example.kotlinweathergr1919.viewViewModel.weatherlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweathergr1919.facade.repo.weatherlist.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<WeatherListState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<WeatherListState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        liveData.value = WeatherListState.Loading
        when ((0..3).random()) {
            in 0..2 -> { outputSuccess(isRussian) }
            3 -> outputError()
        }
    }

    private fun outputSuccess(isRussian: Boolean) = Thread {
        sleep(1000)
        liveData.postValue(
            if (isRussian) {
                WeatherListState.Success(repository.getRussianWeatherFromLocalStorage())
            } else {
                WeatherListState.Success(repository.getWorldWeatherFromLocalStorage())
            }
        )
    }.start()

    private fun outputError() = Thread {
        sleep(1000)
        liveData.postValue(WeatherListState.Error(IllegalAccessException()))
    }.start()
}
