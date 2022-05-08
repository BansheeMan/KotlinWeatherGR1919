package com.example.kotlinweathergr1919

import android.app.Application
import androidx.room.Room
import com.example.kotlinweathergr1919.domain.room.HistoryDao
import com.example.kotlinweathergr1919.domain.room.MyDB
import com.example.kotlinweathergr1919.facade.repo.details.retrofit.WeatherAPI
import com.example.kotlinweathergr1919.utils.YANDEX_DOMAIN
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        val weatherAPI : WeatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        private var db: MyDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appContext != null) {
                    db = Room.databaseBuilder(appContext!!, MyDB::class.java, "HW8").build()
                } else {
                    throw IllegalStateException("CRITICAL ERROR")
                }
            }
            return db!!.historyDao()
        }
    }
}