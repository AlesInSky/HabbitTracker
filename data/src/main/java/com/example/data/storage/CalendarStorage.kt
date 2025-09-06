package com.example.data.storage

import com.example.data.storage.models.CalendarDateData

interface CalendarStorage {

    fun saveCalendar(calendar: CalendarDateData)

    fun getCalendar(): CalendarDateData

}