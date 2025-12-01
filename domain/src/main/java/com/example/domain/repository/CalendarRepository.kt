package com.example.domain.repository

import com.example.domain.models.CalendarDateDomain

interface CalendarRepository {

    fun getCurrentCalendar(): CalendarDateDomain

    fun saveCalendar(calendar: CalendarDateDomain)

}