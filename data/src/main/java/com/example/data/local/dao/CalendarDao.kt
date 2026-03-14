package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CalendarEntity

@Dao
interface CalendarDao {

    @Insert
    suspend fun insert(habit: CalendarEntity)

    @Update
    suspend fun update(habit: CalendarEntity)

    @Delete
    suspend fun delete(habit: CalendarEntity)

    @Query("SELECT * FROM calendar_habits WHERE calendar_date = :calendarDate")
    suspend fun getFromDate(calendarDate: String): List<CalendarEntity>

    @Query("SELECT * FROM calendar_habits ORDER BY calendarId DESC")
    fun getAll(): List<CalendarEntity>
}