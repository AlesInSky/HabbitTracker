package com.example.habbittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.HabitDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.ActualSystemDateDomain
import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay
import com.example.domain.repository.CalendarRepository
import com.example.domain.usecase.GetActualSystemDate
import com.example.domain.usecase.GetHabitListForDayUseCase
import com.example.domain.usecase.GetHabitListForMonthUseCase
import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CalendarViewModel(
    calendarRepository: CalendarRepository,
    val calendarDao: CalendarDao,
    val habitDao: HabitDao,
) : ViewModel() {

    private var _calendarRepository = calendarRepository

    //Переменная для корректного вывода инфы в статистику.
    // Без него в getInfoByID остается подписка на предыдущие месяцы
    private var habitInfoJob: Job? = null

    //Выбор актуального месяца и года
    private var _currentMonthYear = MutableStateFlow(GetMonthUseCase(_calendarRepository).execute())
    val currentMonthYear: StateFlow<CalendarDateDomain> = _currentMonthYear

    private var _systemDate = MutableStateFlow(GetActualSystemDate(_calendarRepository).execute())
    val systemDate: StateFlow<ActualSystemDateDomain> = _systemDate

    //Список уже созданных привычек
    private val _habitList = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habitList: StateFlow<List<HabitEntity>> = _habitList

    //Список дней в месяце, когда имеется привычка
    private val _habitListForMonth = MutableStateFlow<List<Int>>(emptyList())
    val habitListForMonth: StateFlow<List<Int>> = _habitListForMonth

    //Конкретные привычки на определенный день
    private val _habitListForDay = MutableStateFlow<List<HabitForDay>>(emptyList())
    val habitListForDay: StateFlow<List<HabitForDay>> = _habitListForDay


    private val _calendarList = MutableStateFlow<List<String>>(emptyList())
    val calendarList = _calendarList.asStateFlow()

    val dayListInfoDetail = MutableStateFlow<List<CalendarEntity>>(emptyList())

    fun initialization(id: Int) {
        viewModelScope.launch {
            val year = currentMonthYear.value.year
            val month = currentMonthYear.value.month
            val date = "$year-$month-%"
            dayListInfoDetail.value =
                calendarDao.getHabitsForMonthFromId(id = id, calendarDate = date)
        }

    }

    fun updateCalendar() {
        val emptyCells: Int = when (currentMonthYear.value.firstDayOfWeek) {
            1 -> 6
            2 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5
            else -> 7
        }
        val newList = List(emptyCells) { "" } + (1..getDayInMonth()).map { it.toString() }
        _calendarList.value = newList
    }

    fun refresh() {
        updateCalendar()
        getInfoById()
    }

    fun getHabitListForMonth() {
        viewModelScope.launch {
            _habitListForMonth.value =
                GetHabitListForMonthUseCase(calendarRepository = _calendarRepository)
                    .execute(_currentMonthYear.value)
        }
    }

    fun editHabit(
        id: Long,
        habitId: Long,
        price: Float,
        quantity: Float,
        description: String,
        date: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val calendarHabit = CalendarEntity(
                    calendarId = id,
                    calendarHabitId = habitId,
                    calendarHabitPrice = price,
                    calendarHabitQuantity = quantity,
                    calendarHabitDescription = description,
                    calendarDate = date
                )
                calendarDao.update(calendarHabit)
            }
        }
        getHabitListForMonth()
        updateCalendar()
    }

    //Действия с изменением месяца в календаре
    fun getDayInMonth(): Int {
        return _currentMonthYear.value.dayInMonth
    }

    fun getYear(): Int {
        return _currentMonthYear.value.year
    }

    fun getDate(date: Int): Int {
        _currentMonthYear.value = _currentMonthYear.value.copy(date = date)
        return _currentMonthYear.value.date
    }

    fun nextMonth() {
        viewModelScope.launch {
            _currentMonthYear.value = NextMonthUseCase(_calendarRepository).execute()
            getHabitListForMonth()
            updateCalendar()
        }
    }

    fun previousMoth() {
        viewModelScope.launch {
            _currentMonthYear.value = PreviousMonthUseCase(_calendarRepository).execute()
            getHabitListForMonth()
            updateCalendar()
        }
    }

    //Выводим статистику пользователю
    data class HabitUI(
        val id: Int,
        val name: String?,
        val unit: String,
        val sumPrice: Float,
        val sumQuantity: Float,
        val sumDay: Int,
        val image: Int?
    )

    var habitListForMonthUI = MutableStateFlow<List<HabitUI>>(emptyList())

    fun getInfoById() {
        habitInfoJob?.cancel()

        habitInfoJob = viewModelScope.launch {
            val year = currentMonthYear.value.year
            val month = currentMonthYear.value.month
            val date = "$year-$month-%"

            calendarDao.getFromDateForUIList(date).collect { habits ->
                val newList = habits.map { habit ->
                    HabitUI(
                        id = habit.calendarHabitId.toInt(),
                        name = calendarDao.getNameById(habit.calendarHabitId),
                        unit = calendarDao.getUnitById(habit.calendarHabitId),
                        sumPrice = calendarDao.getSumPrice(habit.calendarHabitId, date),
                        sumQuantity = calendarDao.getSumQuantity(habit.calendarHabitId, date),
                        sumDay = calendarDao.getSumDate(habit.calendarHabitId, date),
                        image = calendarDao.getImageById(habit.calendarHabitId)
                    )
                }
                habitListForMonthUI.value = newList.distinctBy { it.name }
            }
        }
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
            _habitListForDay.value =
                GetHabitListForDayUseCase(_calendarRepository).execute(_currentMonthYear.value)
        }
    }

    fun addHabit(habit: CalendarEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                calendarDao.insert(habit)
            }
            getHabitListForMonth()
            loadHabitList()
        }
    }

    fun deleteHabit(habit: CalendarEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                calendarDao.delete(habit)
            }
            getHabitListForMonth()
            loadHabitList()
        }
    }

    init {
        loadHabits()
        getHabitListForMonth()
        getInfoById()
        updateCalendar()
    }
}