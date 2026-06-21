package com.example.data.repository

import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.HabitDao
import com.example.data.storage.CalendarStorage
import com.example.data.storage.models.CalendarDateData
import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay
import com.example.domain.repository.CalendarRepository
import java.util.Calendar

//Логика здесь, потому что репозиторий знает источник данных: класс Calendar.
class CalendarRepositoryImpl(
    private val calendarStorage: CalendarStorage,
    val calendarDao: CalendarDao,
    val habitDao: HabitDao,
) : CalendarRepository {

    override fun getOrCalculateCalendarDate(): CalendarDateDomain {
        val calendar: Calendar = Calendar.getInstance()
        val getCalendar = calendarStorage.getCalendar() //Получаю данные из Data

        if (getCalendar.year == -1 ||
            getCalendar.month == -1 ||
            getCalendar.day == -1 ||
            getCalendar.firstDayOfWeek == -1
        ) {
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            val dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            return CalendarDateDomain(year, month, date, dayInMonth, firstDayOfWeek)
        } else {
            val firstDayOfWeek = getCalendar.firstDayOfWeek
            val month = getCalendar.month
            val year = getCalendar.year
            val date = getCalendar.day
            val dayInMonth = getCalendar.dayInMonth
            return CalendarDateDomain(year, month, date, dayInMonth, firstDayOfWeek)
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

    override suspend fun getHabitListForMonth(calendar: CalendarDateDomain): List<Int> {
        val habitList = mutableListOf<Int>()

        for (day in 1..calendar.dayInMonth) {
            val dao =
                calendarDao.getFromDate(calendarDate = "${calendar.year}-${calendar.month}-${day}")
            if (dao.isNotEmpty()) {
                habitList.add(day)
            }
        }
        return habitList
    }

    override suspend fun getHabitListForDay(calendar: CalendarDateDomain): List<HabitForDay> {
        val currentDate = "${calendar.year}-${calendar.month}-${calendar.date}"
        val calendarEntries = calendarDao.getFromDate(currentDate)

        val habitIds = calendarEntries.map { it.calendarHabitId }
        //val habitDeleted = calendarEntries.map { it.isDeleted }
        val habits = if (habitIds.isNotEmpty()) {
            habitDao.getHabitsByIds(habitIds).associateBy { it.habitId }
        } else {
            emptyMap()
        }


        return calendarEntries.map {
            HabitForDay(
                calendarId = it.calendarId,
                calendarDate = it.calendarDate,
                calendarHabitId = it.calendarHabitId,
                calendarHabitPrice = it.calendarHabitPrice,
                calendarHabitQuantity = it.calendarHabitQuantity,
                calendarHabitDescription = it.calendarHabitDescription,
                habitName = habits[it.calendarHabitId]?.habitName,
                habitUnit = habits[it.calendarHabitId]?.habitUnit,
                habitPrice = habits[it.calendarHabitId]?.habitPrice,
                habitImage = habits[it.calendarHabitId]?.habitImage
            )
        }
        }
    }


