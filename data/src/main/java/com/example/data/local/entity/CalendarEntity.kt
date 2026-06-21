package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_habits")
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true) var calendarId: Long = 0L,
    @ColumnInfo(name = "calendar_habit_id") var calendarHabitId: Long = 0L,
    @ColumnInfo(name = "calendar_habit_price") var calendarHabitPrice: Float = 0F,
    @ColumnInfo(name = "calendar_habit_quantity") var calendarHabitQuantity: Float = 0F,
    @ColumnInfo(name = "calendar_habit_description") var calendarHabitDescription: String = "",
    @ColumnInfo(name = "calendar_date") var calendarDate: String = "2010-01-01"
)