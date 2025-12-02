package com.example.data.storage.models

import java.time.DayOfWeek

data class CalendarDateData(
    var year: Int,
    var month: Int,
    var date: Int,
    var dayInMonth: Int,
    var firstDayOfWeek: Int
)