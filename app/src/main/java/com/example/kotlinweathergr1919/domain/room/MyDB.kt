package com.example.kotlinweathergr1919.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class MyDB : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}