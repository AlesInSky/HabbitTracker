package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitDomain
import com.example.domain.repository.CalendarRepository
import com.example.domain.repository.HabitRepository

class GetHabitList(private val habitRepository: HabitRepository) {

    fun getHabitListUseCase(): HabitDomain {
        return habitRepository.getHabitList()
    }
}