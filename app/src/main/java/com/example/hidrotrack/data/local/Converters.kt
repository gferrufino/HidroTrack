package com.example.hidrotrack.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter fun fromString(s: String?): LocalDate? = s?.let(LocalDate::parse)
    @TypeConverter fun toString(d: LocalDate?): String? = d?.toString()
}
