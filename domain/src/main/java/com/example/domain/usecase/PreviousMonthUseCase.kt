package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository

class PreviousMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(): CalendarDateDomain {

        val todayDate = calendarRepository.getCurrentMonth()
        val newDate = todayDate.copy()

        if (todayDate.month == 0){
            newDate.month = 11
            newDate.year--
        } else newDate.month -=1
        calendarRepository.updateMonth(newDate)
        return todayDate
    }
}