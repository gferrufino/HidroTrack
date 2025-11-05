package com.example.hidrotrack.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.hidrotrack.data.WaterRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class WaterViewModel(app: Application): AndroidViewModel(app) {
    private val repo = WaterRepository.get(app)

    private val _todayCount = MutableLiveData<Int>()
    val todayCount: LiveData<Int> = _todayCount

    val goalFlow = repo.prefs.goalFlow

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _todayCount.value = repo.getTodayCount(LocalDate.now())
    }

    fun addGlass() = viewModelScope.launch { repo.addGlass(1); refresh() }

    fun undo() = viewModelScope.launch {
        val c = _todayCount.value ?: 0
        repo.setTodayCount((c - 1).coerceAtLeast(0)); refresh()
    }

    fun setGoal(goal: Int) = viewModelScope.launch { repo.prefs.setGoal(goal) }
}
