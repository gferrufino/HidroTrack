package com.example.hidrotrack.data

import com.example.hidrotrack.data.local.WaterLog
import com.example.hidrotrack.data.local.WaterLogDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class WaterRepository(private val dao: WaterLogDao) {

    suspend fun getToday(date: LocalDate): WaterLog =
        dao.getByDate(date) ?: WaterLog(date, 0).also { dao.upsert(it) }

    suspend fun increment(date: LocalDate, delta: Int) {
        val current = dao.getByDate(date) ?: WaterLog(date, 0)
        val next = current.copy(count = (current.count + delta).coerceAtLeast(0))
        dao.upsert(next)
    }

    suspend fun setCount(date: LocalDate, count: Int) {
        dao.upsert(WaterLog(date, count.coerceAtLeast(0)))
    }

    // Aumentamos el default para ver más días en Historial
    fun recent(limit: Int = 30): Flow<List<WaterLog>> = dao.recent(limit)
}