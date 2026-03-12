package com.example.habbittracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.HabitDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CalendarViewModel(
    getMonthUseCase: GetMonthUseCase,
    private val nextMonthUseCase: NextMonthUseCase,
    private val previousMonthUseCase: PreviousMonthUseCase,
    val calendarDao: CalendarDao,
    val habitDao: HabitDao,
) : ViewModel() {

    private var _currentMonth = MutableStateFlow(getMonthUseCase.getStartMonth())
    val currentMonth: StateFlow<CalendarDateDomain> = _currentMonth

    private var _currentDate = MutableStateFlow(getMonthUseCase.getStartMonth())
    val currentDate: StateFlow<CalendarDateDomain> = _currentDate

    private val _habitList = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habitList: StateFlow<List<HabitEntity>> = _habitList

    //Действия с изменением месяца в календаре
    fun getDayInMonth(): Int {
        return _currentMonth.value.dayInMonth
    }

    fun getYear(): Int {
        return _currentMonth.value.year
    }

    fun getDate(date: Int): Int {
        _currentDate.value = _currentDate.value.copy(date = date)
        Log.d("ADD3","${_currentDate.value.date}")
        return _currentDate.value.date
    }

    fun nextMonth() {
        val newMonth = nextMonthUseCase.execute()
        _currentMonth.value = newMonth
    }

    fun previousMoth() {
        val newMonth = previousMonthUseCase.execute()
        _currentMonth.value = newMonth
    }

    //Действия с добавлением привычек в календарь

    fun getHabitList(): List<HabitEntity> {
        return _habitList.value
    }

    fun loadHabits() {
        viewModelScope.launch {
            // Загружаем в фоновом потоке IO
            val habits = withContext(Dispatchers.IO) {
                habitDao.getAll()
            }
            _habitList.value = habits
        }
    }

    init {
        loadHabits()
    }

    fun addHabit(habit: CalendarEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                calendarDao.insert(habit)
            }
        }
    }
}