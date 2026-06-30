package com.example.domain.usecase

import com.example.domain.models.ActualSystemDateDomain
import com.example.domain.repository.CalendarRepository

class GetActualSystemDate(private val calendarRepository: CalendarRepository) {
    fun execute(): ActualSystemDateDomain {
        return calendarRepository.actualSystemDate()
    }
}