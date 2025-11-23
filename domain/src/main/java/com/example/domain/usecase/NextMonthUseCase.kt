package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository

class NextMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(): CalendarDateDomain {

        val todayDate = calendarRepository.getCurrentMonth()
        val newDate = todayDate.copy()

        if (todayDate.month == 11 ){
            newDate.month = 0
            newDate.year++
        } else newDate.month +=1
        calendarRepository.updateMonth(newDate)
        return newDate
    }
}