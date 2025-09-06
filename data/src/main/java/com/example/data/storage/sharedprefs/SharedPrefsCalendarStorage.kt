package com.example.data.storage.sharedprefs

import android.content.Context
import androidx.core.content.edit
import com.example.data.storage.CalendarStorage
import com.example.data.storage.models.CalendarDateData

private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"
private const val YEAR = "YEAR"
private const val MONTH = "MONTH"
private const val DAY_TO_MONTH = "DAY_TO_MONTH"

class SharedPrefsCalendarStorage(context: Context): CalendarStorage {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveCalendar(calendar: CalendarDateData) {
        sharedPreferences.edit {
            putInt(YEAR, calendar.year)
            putInt(MONTH, calendar.month)
            putInt(DAY_TO_MONTH, calendar.days)
        }
    }

    override fun getCalendar(): CalendarDateData {
        val year = sharedPreferences.getInt(YEAR, 2000)
        val month = sharedPreferences.getInt(MONTH, 0)
        val dayToMonth = sharedPreferences.getInt(DAY_TO_MONTH, 1)

        return CalendarDateData(year,month,dayToMonth)
    }

}