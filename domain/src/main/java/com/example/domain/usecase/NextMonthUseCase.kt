package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

class NextMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(): CalendarDateDomain {

        val todayDate = calendarRepository.getCurrentCalendar()
        val newDate = todayDate.copy()

        if (todayDate.month == 11) {
            newDate.month = 0
            newDate.dayInMonth = Calendar.DAY_OF_MONTH
            newDate.year++
        } else newDate.month += 1

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, newDate.year)
        calendar.set(Calendar.MONTH, newDate.month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        newDate.firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        newDate.dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendarRepository.saveCalendar(newDate)
        return newDate
    }
}