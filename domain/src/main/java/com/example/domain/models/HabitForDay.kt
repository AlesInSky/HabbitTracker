package com.example.domain.models

data class HabitForDay(
    val calendarId: Long,
    val calendarDate: String,
    val calendarHabitId: Long,
    val calendarHabitPrice: Float,
    val calendarHabitQuantity: Float,
    val calendarHabitDescription: String?,
    val habitName: String?,
    val habitUnit: String?,
    val habitPrice: Int?,
    val habitImage: Int?
)
