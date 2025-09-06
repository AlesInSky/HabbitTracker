package com.example.data.repository

import com.example.data.storage.CalendarStorage
import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

//Логика здесь, потому что репозиторий знает источник данных: класс Calendar.

class CalendarRepositoryImpl(private val calendarStorage: CalendarStorage) : CalendarRepository {
    override fun getNowMonth(): CalendarDateDomain {
        val calendar: Calendar = Calendar.getInstance()
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        var date = calendar.get(Calendar.DAY_OF_MONTH)

        return CalendarDateDomain(year, month, date )
    }

    override fun getNextMonth(): CalendarDateDomain {
        TODO("Not yet implemented")
    }

    override fun getPreviousMonth(): CalendarDateDomain {
        TODO("Not yet implemented")
    }
}