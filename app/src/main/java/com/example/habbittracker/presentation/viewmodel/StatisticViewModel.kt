package com.example.habbittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.HabitDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class StatisticPeriod {
    WEEK, MONTH, YEAR, ALL
}

enum class StatisticChartType {
    BAR, LINE, PIE
}

data class HabitStatisticItem(
    val habitId: Long,
    val name: String,
    val unit: String,
    val image: Int?,
    val totalExpense: Float,
    val totalQuantity: Float,
    val recordsCount: Int,
    val daysCount: Int,
    val isDeleted: Boolean,
)

data class StatisticExpensePoint(
    val label: String,
    val amount: Float,
    val values: Map<Long, Float> = emptyMap(),
)

data class StatisticUiState(
    val isLoading: Boolean = true,
    val period: StatisticPeriod = StatisticPeriod.MONTH,
    val chartType: StatisticChartType = StatisticChartType.BAR,

    // null означает, что выбраны все привычки
    val selectedHabitIds: Set<Long>? = null,

    val showDeletedHabits: Boolean = true,
    val totalExpenses: Float = 0f,
    val recordsCount: Int = 0,
    val activeDaysCount: Int = 0,
    val habits: List<HabitStatisticItem> = emptyList(),
    val expensePoints: List<StatisticExpensePoint> = emptyList(),
)

class StatisticViewModel(
    private val calendarDao: CalendarDao,
    private val habitDao: HabitDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticUiState())
    val uiState: StateFlow<StatisticUiState> = _uiState.asStateFlow()

    private var allHabitStatistics: List<HabitStatisticItem> = emptyList()
    private var allCalendarEntries: List<CalendarEntity> = emptyList()
    private var allHabits: List<HabitEntity> = emptyList()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                calendarDao.getAllForStatistics() to habitDao.getAll()
            }

            allCalendarEntries = result.first
            allHabits = result.second

            calculateStatistics()
        }
    }

    fun selectPeriod(period: StatisticPeriod) {
        if (_uiState.value.period == period) return

        _uiState.value = _uiState.value.copy(period = period)
        calculateStatistics()
    }

    fun selectChartType(type: StatisticChartType) {
        _uiState.value = _uiState.value.copy(chartType = type)
    }

    fun nextChartType() {
        val next = when (_uiState.value.chartType) {
            StatisticChartType.BAR -> StatisticChartType.LINE
            StatisticChartType.LINE -> StatisticChartType.PIE
            StatisticChartType.PIE -> StatisticChartType.BAR
        }
        selectChartType(next)
    }

    fun previousChartType() {
        val previous = when (_uiState.value.chartType) {
            StatisticChartType.BAR -> StatisticChartType.PIE
            StatisticChartType.LINE -> StatisticChartType.BAR
            StatisticChartType.PIE -> StatisticChartType.LINE
        }
        selectChartType(previous)
    }

    fun toggleHabitSelection(habitId: Long) {
        val allIds = allHabitStatistics.map { it.habitId }.toSet()
        if (habitId !in allIds) return

        val currentSelection = _uiState.value.selectedHabitIds ?: allIds

        val newSelection = if (habitId in currentSelection) {
            currentSelection - habitId
        } else {
            currentSelection + habitId
        }

        // Если сняли выбор со всех или снова выбрали все —
        // возвращаем режим "Все привычки".
        val selection = when {
            newSelection.isEmpty() -> null
            newSelection.size == allIds.size -> null
            else -> newSelection
        }

        _uiState.value = _uiState.value.copy(
            selectedHabitIds = selection
        )
    }

    fun setShowDeletedHabits(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showDeletedHabits = show,
            habits = if (show) {
                allHabitStatistics
            } else {
                allHabitStatistics.filterNot { it.isDeleted }
            }
        )
    }

    private fun calculateStatistics() {
        val today = LocalDate.now()

        val parsedEntries = allCalendarEntries.mapNotNull { entry ->
            val date = parseDate(entry.calendarDate) ?: return@mapNotNull null
            entry to date
        }

        val period = _uiState.value.period

        val periodEntries = when (period) {
            StatisticPeriod.WEEK -> {
                val start = today.with(DayOfWeek.MONDAY)
                val end = start.plusDays(6)

                parsedEntries.filter { (_, date) ->
                    !date.isBefore(start) && !date.isAfter(end)
                }
            }

            StatisticPeriod.MONTH -> {
                val start = today.withDayOfMonth(1)
                val end = today.withDayOfMonth(today.lengthOfMonth())

                parsedEntries.filter { (_, date) ->
                    !date.isBefore(start) && !date.isAfter(end)
                }
            }

            StatisticPeriod.YEAR -> {
                val start = today.withDayOfYear(1)
                val end = today.withDayOfYear(today.lengthOfYear())

                parsedEntries.filter { (_, date) ->
                    !date.isBefore(start) && !date.isAfter(end)
                }
            }

            StatisticPeriod.ALL -> parsedEntries
        }

        val habitsById = allHabits.associateBy { it.habitId }

        allHabitStatistics = periodEntries
            .groupBy { it.first.calendarHabitId }
            .map { (habitId, records) ->
                val habit = habitsById[habitId]

                HabitStatisticItem(
                    habitId = habitId,
                    name = habit?.habitName ?: "Привычка удалена",
                    unit = habit?.habitUnit ?: "",
                    image = habit?.habitImage,
                    totalExpense = records.sumOf {
                        it.first.calendarHabitPrice.toDouble()
                    }.toFloat(),
                    totalQuantity = records.sumOf {
                        it.first.calendarHabitQuantity.toDouble()
                    }.toFloat(),
                    recordsCount = records.size,
                    daysCount = records.map { it.second }.distinct().size,
                    isDeleted = habit == null || habit.isDeleted
                )
            }
            .sortedByDescending { it.totalExpense }

        val totalExpenses = periodEntries.sumOf {
            it.first.calendarHabitPrice.toDouble()
        }.toFloat()

        val activeDaysCount = periodEntries.map { it.second }.distinct().size

        val expensePoints = createExpensePoints(periodEntries, period)

        val showDeleted = _uiState.value.showDeletedHabits

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            totalExpenses = totalExpenses,
            recordsCount = periodEntries.size,
            activeDaysCount = activeDaysCount,
            habits = if (showDeleted) {
                allHabitStatistics
            } else {
                allHabitStatistics.filterNot { it.isDeleted }
            },
            expensePoints = expensePoints
        )
    }

    private fun createExpensePoints(
        entries: List<Pair<CalendarEntity, LocalDate>>,
        period: StatisticPeriod,
    ): List<StatisticExpensePoint> {

        val buckets: List<Pair<String, (LocalDate) -> Boolean>> =
            when (period) {
                StatisticPeriod.WEEK -> {
                    val start = LocalDate.now().with(DayOfWeek.MONDAY)

                    (0..6).map { offset ->
                        val date = start.plusDays(offset.toLong())

                        date.dayOfMonth.toString() to { entryDate ->
                            entryDate == date
                        }
                    }
                }

                StatisticPeriod.MONTH -> {
                    val month = YearMonth.now()

                    (1..month.lengthOfMonth()).map { day ->
                        val date = month.atDay(day)

                        day.toString() to { entryDate ->
                            entryDate == date
                        }
                    }
                }

                StatisticPeriod.YEAR -> {
                    val year = LocalDate.now().year

                    (1..12).map { month ->
                        val currentMonth = YearMonth.of(year, month)

                        currentMonth.month.getDisplayName(
                            java.time.format.TextStyle.SHORT,
                            Locale("ru")
                        ) to { entryDate ->
                            YearMonth.from(entryDate) == currentMonth
                        }
                    }
                }

                StatisticPeriod.ALL -> {
                    if (entries.isEmpty()) {
                        emptyList<Pair<String, (LocalDate) -> Boolean>>()
                    } else {
                        val firstMonth = YearMonth.from(
                            entries.minOf { it.second }
                        )

                        val lastMonth = YearMonth.from(
                            entries.maxOf { it.second }
                        )

                        val formatter =
                            DateTimeFormatter.ofPattern("MMM yy", Locale("ru"))

                        generateSequence(firstMonth) { month ->
                            if (month < lastMonth) {
                                month.plusMonths(1)
                            } else {
                                null
                            }
                        }.map { month ->
                            Pair<String, (LocalDate) -> Boolean>(
                                month.format(formatter),
                                { entryDate ->
                                    YearMonth.from(entryDate) == month
                                }
                            )
                        }.toList()
                    }
                }
            }

        return buckets.map { (label, matchesDate) ->
            val bucketEntries = entries.filter { (_, date) ->
                matchesDate(date)
            }

            val values = bucketEntries
                .groupBy { it.first.calendarHabitId }
                .mapValues { (_, records) ->
                    records.sumOf {
                        it.first.calendarHabitPrice.toDouble()
                    }.toFloat()
                }

            StatisticExpensePoint(
                label = label,
                amount = values.values.sum(),
                values = values
            )
        }
    }

    private fun parseDate(value: String): LocalDate? {
        return try {
            val parts = value.split("-")
            if (parts.size != 3) return null

            LocalDate.of(
                parts[0].toInt(),
                parts[1].toInt(),
                parts[2].toInt()
            )
        } catch (_: Exception) {
            null
        }
    }
}