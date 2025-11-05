package com.example.hidrotrack.data.local

import androidx.room.*
import java.time.LocalDate

@Entity(tableName = "water_log")
data class WaterLog(
    @PrimaryKey val date: LocalDate,
    val count: Int
)

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_log WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: LocalDate): WaterLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: WaterLog)

    @Query("SELECT * FROM water_log ORDER BY date DESC LIMIT :days")
    suspend fun recent(days: Int = 7): List<WaterLog>
}

@Database(entities = [WaterLog::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun waterLogDao(): WaterLogDao
}
