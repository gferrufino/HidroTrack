package com.example.hidrotrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey val date: LocalDate,
    val count: Int
)
