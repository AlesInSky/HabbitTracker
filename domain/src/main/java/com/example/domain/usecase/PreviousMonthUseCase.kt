package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

class PreviousMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(): CalendarDateDomain {

        val todayDate = calendarRepository.getCurrentCalendar()
        val newDate = todayDate.copy()

        if (todayDate.month == 0) {
            newDate.month = 11
            newDate.dayInMonth = Calendar.DAY_OF_MONTH
            newDate.year--
        } else newDate.month -= 1

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, newDate.year)
        calendar.set(Calendar.MONTH, newDate.month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        newDate.dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendarRepository.saveCalendar(newDate)
        return newDate
    }
}