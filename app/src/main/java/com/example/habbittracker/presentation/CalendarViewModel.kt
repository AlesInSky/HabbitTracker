package com.example.habbittracker.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.data.storage.models.CalendarDateData
import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase

class CalendarViewModel(
    private val getMonthUseCase: GetMonthUseCase,
    private val nextMonthUseCase: NextMonthUseCase,
    private val previousMonthUseCase: PreviousMonthUseCase,
) : ViewModel() {


    var currentMonth = mutableStateOf(
        CalendarDateData(
            getMonthUseCase.execute().year,
            getMonthUseCase.execute().month,
            getMonthUseCase.execute().days
        )
    )

    fun getMonth(): String {
        return currentMonth.value.month.toString()
    }

    fun nextMonth() {
        val newMonth = nextMonthUseCase.execute().month
            currentMonth.value = currentMonth.value.copy(month = newMonth)
    }

    fun previousMoth() {
        val newMonth = previousMonthUseCase.execute().month
        currentMonth.value = currentMonth.value.copy(month = newMonth)
    }

}