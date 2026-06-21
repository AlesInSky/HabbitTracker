package com.example.domain.repository

import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay

interface CalendarRepository {

    fun getOrCalculateCalendarDate(): CalendarDateDomain

    fun saveCalendar(calendar: CalendarDateDomain)

    suspend fun getHabitListForMonth(calendar: CalendarDateDomain): List<Int>

    suspend fun getHabitListForDay(calendar: CalendarDateDomain): List<HabitForDay>

}