package com.example.kotlinweathergr1919.domain.room

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("INSERT INTO history_table (city, temperature, feelsLike, icon) VALUES(:city,:temperature,:feelsLike,:icon)")
    fun nativeInsert(city: String, temperature: Int, feelsLike: Int, icon: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM history_table WHERE city=:city")
    fun getHistoryCity(city: String): List<HistoryEntity>
}