package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository


class GetHabitListForMonthUseCase(private val calendarRepository: CalendarRepository) {
    suspend fun execute(calendar: CalendarDateDomain): List<Int> {
        return calendarRepository.getHabitListForMonth(calendar)
    }
}