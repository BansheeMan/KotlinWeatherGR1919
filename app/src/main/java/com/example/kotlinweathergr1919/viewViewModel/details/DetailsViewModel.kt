package com.example.kotlinweathergr1919.viewViewModel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.repo.details.DetailsRepository
import com.example.kotlinweathergr1919.facade.repo.details.retrofit.DetailsRepositoryRetrofit2Impl

class DetailsViewModel(
    private val liveData: MutableLiveData<ResponseState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(ResponseState.Loading)
        repository.getWeatherDetails(city, object : OnServersResponse {
            override fun onResponse(responseState: ResponseState) {
                liveData.postValue(responseState)
            }
        })
    }
}