package com.example.hidrotrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WaterLogDao {
    @Upsert
    suspend fun upsert(log: WaterLog)

    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: LocalDate): WaterLog?

    @Query("SELECT * FROM water_logs ORDER BY date DESC LIMIT :limit")
    fun recent(limit: Int): Flow<List<WaterLog>>
}
