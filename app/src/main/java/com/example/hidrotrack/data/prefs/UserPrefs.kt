package com.example.hidrotrack.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")
object Keys { val DAILY_GOAL = intPreferencesKey("daily_goal") }

class UserPrefs(private val context: Context) {
    val goalFlow = context.dataStore.data.map { it[Keys.DAILY_GOAL] ?: 8 }
    suspend fun setGoal(goal: Int) = context.dataStore.edit {
        it[Keys.DAILY_GOAL] = goal.coerceIn(1, 30)
    }
}
