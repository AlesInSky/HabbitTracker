package com.example.domain.repository

import com.example.domain.models.CalendarDateDomain

interface CalendarRepository {

    fun getNowMonth(): CalendarDateDomain

    fun getNextMonth(): CalendarDateDomain

    fun getPreviousMonth(): CalendarDateDomain

}