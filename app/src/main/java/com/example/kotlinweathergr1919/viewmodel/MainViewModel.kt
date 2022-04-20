package com.example.kotlinweathergr1919.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweathergr1919.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)

    private fun getWeather(isRussian: Boolean) {
        liveData.value = AppState.Loading
        when ((0..1).random()) {
            0 -> outputSuccess(isRussian)
            1 -> outputError()
        }
    }

    private fun outputSuccess(isRussian: Boolean) = Thread {
        sleep(1000)
        liveData.postValue(
            if (isRussian) {
                AppState.Success(repository.getRussianWeatherFromLocalStorage())
            } else {
                AppState.Success(repository.getWorldWeatherFromLocalStorage())
            }
        )
    }.start()

    private fun outputError() = Thread {
        sleep(1000)
        liveData.postValue(AppState.Error(IllegalAccessException()))
    }.start()
}
