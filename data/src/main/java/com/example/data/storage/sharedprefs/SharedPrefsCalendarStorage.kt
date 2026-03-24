package com.example.data.storage.sharedprefs

import android.content.Context
import androidx.core.content.edit
import com.example.data.storage.CalendarStorage
import com.example.data.storage.models.CalendarDateData

private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"
private const val YEAR = "YEAR"
private const val MONTH = "MONTH"
private const val DATE = "DATE"
private const val DAY_IN_MONTH = "DAY_IN_MONTH"
private const val FIRST_DAY_IN_MONTH = "FIRST_DAY_IN_MONTH"

class SharedPrefsCalendarStorage(context: Context) : CalendarStorage {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveCalendar(calendar: CalendarDateData) {
        sharedPreferences.edit {
            putInt(YEAR, calendar.year)
            putInt(MONTH, calendar.month)
            putInt(DATE, calendar.day)
            putInt(DAY_IN_MONTH, calendar.dayInMonth)
            putInt(FIRST_DAY_IN_MONTH, calendar.firstDayOfWeek)
        }
    }

    override fun getCalendar(): CalendarDateData {
        val year = sharedPreferences.getInt(YEAR, -1)
        val month = sharedPreferences.getInt(MONTH, -1)
        val date = sharedPreferences.getInt(DATE, -1)
        val dayInMonth = sharedPreferences.getInt(DAY_IN_MONTH, -1)
        val firstDayOfWeek = sharedPreferences.getInt(FIRST_DAY_IN_MONTH, -1)
        return CalendarDateData(year, month, date, dayInMonth, firstDayOfWeek)
    }
}