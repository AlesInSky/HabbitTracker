package com.example.data.repository

import com.example.data.storage.CalendarStorage
import com.example.data.storage.models.CalendarDateData
import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

//Логика здесь, потому что репозиторий знает источник данных: класс Calendar.

class CalendarRepositoryImpl(private val calendarStorage: CalendarStorage) : CalendarRepository {

    override fun getCurrentMonth(): CalendarDateDomain {
        val calendar: Calendar = Calendar.getInstance()
        var month: Int
        var year: Int
        var date: Int
        //var weekDay: Int

        if (calendarStorage.getCalendar().year == -1 ||
            calendarStorage.getCalendar().month == -1 ||
            calendarStorage.getCalendar().days == -1
        ) {
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            date = calendar.get(Calendar.DAY_OF_MONTH)
            //weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        } else {
            month = calendarStorage.getCalendar().month
            year = calendarStorage.getCalendar().year
            date = calendarStorage.getCalendar().days
        }
        return CalendarDateDomain(year, month, date)
    }

    //Берем данные из CalendarDateDomain и перекидываем их в CalendarDateData
    override fun updateMonth(calendar: CalendarDateDomain) {
        calendarStorage.saveCalendar(
            CalendarDateData(
                calendar.year,
                calendar.month,
                calendar.days
            )
        )
    }
}