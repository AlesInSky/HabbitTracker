package com.example.habbittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.dao.HabitDao
import com.example.data.local.entity.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitViewModel(val dao: HabitDao) : ViewModel() {
    private val _habitList = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habitList: StateFlow<List<HabitEntity>> = _habitList

    private val _editHabit = MutableStateFlow<HabitEntity?>(null)
    val editHabit: StateFlow<HabitEntity?> = _editHabit

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            val habits = withContext(Dispatchers.IO) {
                dao.getAll()
            }
            _habitList.value = habits
        }
    }

    fun openEditDialog(habit: HabitEntity) {
        _editHabit.value = habit.copy()
    }

    fun closeEditDialog() {
        _editHabit.value = null
    }

    fun addHabit(newHabit: HabitEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(newHabit)
            }
            loadHabits()
        }
    }

    fun updateHabit(updateHabit: HabitEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.update(updateHabit)  // обновляем в базе
            }
            loadHabits()  // перезагружаем список
            closeEditDialog()  // закрываем диалог
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.delete(habit)
            }
            loadHabits()
        }
    }
}