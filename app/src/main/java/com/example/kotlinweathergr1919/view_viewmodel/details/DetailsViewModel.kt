package com.example.kotlinweathergr1919.view_viewmodel.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.repo.details.DetailsRepository
import com.example.kotlinweathergr1919.facade.repo.details.retrofit.DetailsRepositoryRetrofit2Impl
import com.example.kotlinweathergr1919.facade.repo.historyweather.HistoryRepositoryAdd
import com.example.kotlinweathergr1919.facade.repo.historyweather.HistoryRepositoryRoomImpl
import com.example.kotlinweathergr1919.view_viewmodel.details.ResponseState.Success

class DetailsViewModel(
    private val liveData: MutableLiveData<ResponseState> = MutableLiveData(),
    private val repositoryAdd: HistoryRepositoryAdd = HistoryRepositoryRoomImpl()
) : ViewModel() {

    private var repository: DetailsRepository = DetailsRepositoryRetrofit2Impl()

    fun getLiveData() = liveData
    fun getWeather(city: City) {
        liveData.postValue(ResponseState.Loading)
        repository = if(isInternet) {
            DetailsRepositoryRetrofit2Impl()

        } else {
            HistoryRepositoryRoomImpl()
        }
        Log.d("@@@", "repository $repository")
        repository.getWeatherDetails(city, object : OnServersResponse {
            override fun onResponse(responseState: ResponseState) {
                liveData.postValue(responseState)
                if(isInternet){
                    if(responseState is Success) {
                        repositoryAdd.addWeather(responseState.weather)
                    }
                }
            }
        })
    }
}

private var isInternet = false
var networkState : BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        isInternet = !noConnectivity
        Log.d("@@@", "isInternet $isInternet")
    }
}

