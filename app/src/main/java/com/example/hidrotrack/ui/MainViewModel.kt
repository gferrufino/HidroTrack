package com.example.hidrotrack.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.hidrotrack.data.DataStoreManager
import com.example.hidrotrack.data.WaterRepository
import com.example.hidrotrack.data.local.AppDb
import com.example.hidrotrack.data.local.WaterLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = Room.databaseBuilder(app, AppDb::class.java, "hidrotrack.db").build()
    private val repo = WaterRepository(db.waterLogDao())
    private val store = DataStoreManager(app)

    private val today: LocalDate = LocalDate.now()

    private val _todayCount = MutableStateFlow(0)
    val todayCount: StateFlow<Int> = _todayCount.asStateFlow()

    val goalFlow: Flow<Int> = store.goalFlow
    val recentFlow: Flow<List<WaterLog>> = repo.recent(30)

    init {
        viewModelScope.launch {
            val log = repo.getToday(today)
            _todayCount.value = log.count
        }
    }

    fun addOne() = viewModelScope.launch {
        repo.increment(today, +1)
        _todayCount.value = _todayCount.value + 1
    }

    fun removeOne() = viewModelScope.launch {
        if (_todayCount.value > 0) {
            repo.increment(today, -1)
            _todayCount.value = _todayCount.value - 1
        }
    }

    fun setGoal(goal: Int) = viewModelScope.launch {
        store.setGoal(goal)
    }

    suspend fun saveToday() {
        repo.setCount(today, _todayCount.value)
    }
}

class MainVMFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app) as T
    }
}
