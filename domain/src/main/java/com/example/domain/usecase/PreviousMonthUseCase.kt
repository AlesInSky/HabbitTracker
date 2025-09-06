package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository

class PreviousMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(month: Int, year: Int): CalendarDateDomain {

        val todayDate = calendarRepository.getNowMonth()
        val newDate = todayDate
        if (todayDate.month > 11){
            newDate.month = 0
            newDate.year++
        }

        return calendarRepository.getPreviousMonth()
    }

}