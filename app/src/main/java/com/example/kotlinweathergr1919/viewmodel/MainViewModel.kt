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

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveData.value = AppState.Loading
        when((0..1).random()) {
            1 -> outputSuccess()
            0 -> outputError()
        }
    }

    private fun outputSuccess() = Thread {
        sleep(1000)
        liveData.postValue(AppState.Success(repository.getWeatherFromLocalStorage()))
    }.start()

    private fun outputError() = Thread {
        sleep(1000)
        liveData.postValue(AppState.Error(Exception("502 Bad Gateway")))
    }.start()
}