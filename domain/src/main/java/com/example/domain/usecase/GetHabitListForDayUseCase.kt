package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay
import com.example.domain.repository.CalendarRepository

class GetHabitListForDayUseCase(private val calendarRepository: CalendarRepository) {

    suspend fun execute(calendar: CalendarDateDomain): List<HabitForDay> {
    return calendarRepository.getHabitListForDay(calendar)
    }
}