package com.example.habbittracker.presentation.viewmodel

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

class CalendarViewModel(
    getMonthUseCase: GetMonthUseCase,
    private val nextMonthUseCase: NextMonthUseCase,
    private val previousMonthUseCase: PreviousMonthUseCase,
    val calendarDao: CalendarDao,
    val habitDao: HabitDao,
) : ViewModel() {

    private var _currentMonth = MutableStateFlow(getMonthUseCase.getStartMonth())
    val currentMonth: StateFlow<CalendarDateDomain> = _currentMonth

    val calendarDay = 10

    private val _editCalendarHabit = MutableStateFlow<CalendarEntity?>(null)
    val editCalendarHabit: StateFlow<CalendarEntity?> = _editCalendarHabit

    private val _habitList = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habitList: StateFlow<List<HabitEntity>> = _habitList


    //Действия с изменением месяца в календаре
    fun getMonth(): Int {
        return _currentMonth.value.month
    }

    fun getDayInMonth(): Int {
        return _currentMonth.value.dayInMonth
    }

    fun getFirstDayOfWeek(): Int {
        val firstDay = _currentMonth.value.firstDayOfWeek
        return firstDay
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

    private fun loadHabits() {
        viewModelScope.launch {
            // Загружаем в фоновом потоке IO
            val habits = withContext(Dispatchers.IO) {
                habitDao.getAll()
            }
            _habitList.value = habits
        }
    }

    fun openEditDialog(calendar: CalendarEntity) {
        _editCalendarHabit.value = calendar.copy()
    }

    fun closeEditDialog() {
        _editCalendarHabit.value = null
    }

    fun addHabit() {
        viewModelScope.launch {
            val calendar = CalendarEntity()
            calendar.calendarDate = calendarDay.toString()
            withContext(Dispatchers.IO){
                calendarDao.insert(calendar)
            }
        }
    }
}