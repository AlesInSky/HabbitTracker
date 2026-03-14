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

    //Выбор актуального месяца и года
    private var _currentMonth = MutableStateFlow(getMonthUseCase.getStartMonth())
    val currentMonth: StateFlow<CalendarDateDomain> = _currentMonth

    //Выбор актуального дня
    private var _currentDate = MutableStateFlow(getMonthUseCase.getStartMonth())
    val currentDate: StateFlow<CalendarDateDomain> = _currentDate

    //Список уже созданных привычек
    private val _habitList = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habitList: StateFlow<List<HabitEntity>> = _habitList

    //Список дней, когда имеется привычка
    private val _habitListForDate = MutableStateFlow<List<Int>>(emptyList())
    val habitListForDate: StateFlow<List<Int>> = _habitListForDate

    //Конкретные привычки на определенный день
    private val _habitListFromDate = MutableStateFlow<List<Map<String, Any?>>>(emptyList())
    val habitListFromDate: StateFlow<List<Map<String, Any?>>> = _habitListFromDate

    fun getHabitDay() {
        viewModelScope.launch {
            val year = _currentMonth.value.year
            val month = _currentMonth.value.month
            val dayInMonth = _currentMonth.value.dayInMonth
            var date = 1
            val habitList = mutableListOf<Int>()

            while (date <= dayInMonth) {
                val currentDate = "${year}-${month}-${date}"
                val getHabitForDate = calendarDao.getFromDate(calendarDate = currentDate)
                if (getHabitForDate.isNotEmpty()) {
                    habitList.add("$date".toInt())
                }
                date++
            }
            _habitListForDate.value = habitList
            Log.d("ADD2", "$_habitListForDate")

        }
    }

    //Действия с изменением месяца в календаре
    fun getDayInMonth(): Int {
        return _currentMonth.value.dayInMonth
    }

    fun getYear(): Int {
        return _currentMonth.value.year
    }

    fun getDate(date: Int): Int {
        _currentDate.value = _currentDate.value.copy(date = date)
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
    //Загрузка списка всех привычек
    fun loadHabits() {
        viewModelScope.launch {
            val habits = withContext(Dispatchers.IO) {
                habitDao.getAll()
            }
            _habitList.value = habits
        }
    }

    //Загрузка привычек на день недели
    fun loadHabitList() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                val year = _currentMonth.value.year
                val month = _currentMonth.value.month
                val date = _currentDate.value.date
                val currentDate = "${year}-${month}-${date}"

                val calendarEntries = calendarDao.getFromDate(currentDate)

                val habitIds = calendarEntries.map { it.calendarHabitId }
                val habits = if (habitIds.isNotEmpty()) {
                    habitDao.getHabitsByIds(habitIds).associateBy { it.habitId }
                } else {
                    emptyMap()
                }

                calendarEntries.map { entry ->
                    mapOf(
                        "calendarId" to entry.calendarId,
                        "calendar_date" to entry.calendarDate,
                        "calendar_habit_id" to entry.calendarHabitId,
                        "calendar_habit_price" to entry.calendarHabitPrice,
                        "calendar_habit_quantity" to entry.calendarHabitQuantity,
                        "calendar_habit_description" to entry.calendarHabitDescription,
                        "habit_name" to habits[entry.calendarHabitId]?.habitName,
                        "habit_unit" to habits[entry.calendarHabitId]?.habitUnit,
                        "habit_price" to habits[entry.calendarHabitId]?.habitPrice,
                        "habit_description" to habits[entry.calendarHabitId]?.habitDescription,
                        "habit_image" to habits[entry.calendarHabitId]?.habitImage
                    )
                }
            }
            _habitListFromDate.value = result
        }
    }

    fun addHabit(habit: CalendarEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                calendarDao.insert(habit)
            }
            getHabitDay()
        }
    }

    fun deleteHabit(habit: CalendarEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                calendarDao.delete(habit)
            }
            getHabitDay()
            loadHabitList()
        }
    }

    init {
        loadHabits()
        getHabitDay()
    }
}