package com.example.hidrotrack.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager(private val context: Context) {
    companion object {
        private val KEY_DAILY_GOAL = intPreferencesKey("daily_goal")
    }

    val goalFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_DAILY_GOAL] ?: 8
    }

    suspend fun setGoal(value: Int) {
        context.dataStore.edit { it[KEY_DAILY_GOAL] = value }
    }
}
