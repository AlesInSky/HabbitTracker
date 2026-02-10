package com.example.data.repository

import com.example.data.storage.CalendarStorage
import com.example.data.storage.models.CalendarDateData
import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

//Логика здесь, потому что репозиторий знает источник данных: класс Calendar.
class CalendarRepositoryImpl(private val calendarStorage: CalendarStorage) : CalendarRepository {

    override fun getCurrentCalendar(): CalendarDateDomain {
        val calendar: Calendar = Calendar.getInstance()
        val getCalendar = calendarStorage.getCalendar() //Получаю данные из Data

        if (getCalendar.year == -1 ||
            getCalendar.month == -1 ||
            getCalendar.date == -1 ||
            getCalendar.firstDayOfWeek == -1
        ) {
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            val dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            return CalendarDateDomain(year, month, date, dayInMonth,firstDayOfWeek)
        } else {
            val firstDayOfWeek = getCalendar.firstDayOfWeek
            val month = getCalendar.month
            val year = getCalendar.year
            val date = getCalendar.date
            val dayInMonth = getCalendar.dayInMonth
            return CalendarDateDomain(year, month, date, dayInMonth,firstDayOfWeek)
        }
    }

    //Берем данные из CalendarDateDomain и перекидываем их в CalendarDateData
    override fun saveCalendar(calendar: CalendarDateDomain) {
        calendarStorage.saveCalendar(
            CalendarDateData(
                calendar.year,
                calendar.month,
                calendar.date,
                calendar.dayInMonth,
                calendar.firstDayOfWeek
            )
        )
    }
}
