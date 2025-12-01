package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository

class GetMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun getStartMonth(): CalendarDateDomain {
        return calendarRepository.getCurrentCalendar()
    }
}