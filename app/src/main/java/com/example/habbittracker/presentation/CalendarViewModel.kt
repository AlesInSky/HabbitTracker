package com.example.habbittracker.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase

class CalendarViewModel(
    getMonthUseCase: GetMonthUseCase,
    private val nextMonthUseCase: NextMonthUseCase,
    private val previousMonthUseCase: PreviousMonthUseCase,
) : ViewModel() {

    var currentMonth = mutableStateOf(getMonthUseCase.getStartMonth())

    fun getMonth(): Int {
        return currentMonth.value.month
    }

    fun getDayInMonth(): Int {
        return currentMonth.value.dayInMonth
    }

    fun getFirstDayOfWeek(): Int {
        val firstDay = currentMonth.value.firstDayOfWeek
        when(firstDay){
            1 -> 7
            2 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5

        }
        return firstDay
    }

    fun nextMonth() {
        val newMonth = nextMonthUseCase.execute()
        currentMonth.value = newMonth
    }

    fun previousMoth() {
        val newMonth = previousMonthUseCase.execute()
        currentMonth.value = newMonth
    }
}