package com.example.domain.repository

import com.example.domain.models.HabitDomain

interface HabitRepository {

    fun getHabitList(): HabitDomain

    fun saveHabitList(habitList: HabitDomain)
}