package com.example.domain.repository

import com.example.domain.models.CalendarDateDomain

interface CalendarRepository {

    fun getCurrentMonth(): CalendarDateDomain

    fun updateMonth(calendar: CalendarDateDomain)

}