package com.example.hidrotrack.data

import android.content.Context
import androidx.room.Room
import com.example.hidrotrack.data.local.*
import com.example.hidrotrack.data.prefs.UserPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class WaterRepository private constructor(context: Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, "hidrotack.db").build()
    private val dao = db.waterLogDao()
    val prefs = UserPrefs(context)

    suspend fun getTodayCount(date: LocalDate = LocalDate.now()): Int =
        dao.getByDate(date)?.count ?: 0

    suspend fun setTodayCount(count: Int, date: LocalDate = LocalDate.now()) {
        dao.upsert(WaterLog(date, count.coerceAtLeast(0)))
    }

    suspend fun addGlass(n: Int = 1) {
        val today = LocalDate.now()
        val current = getTodayCount(today)
        setTodayCount(current + n, today)
    }

    fun lastDays(days: Int = 7): Flow<List<WaterLog>> = flow { emit(dao.recent(days)) }

    companion object {
        @Volatile private var INSTANCE: WaterRepository? = null
        fun get(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: WaterRepository(context.applicationContext).also { INSTANCE = it }
        }
    }
}
