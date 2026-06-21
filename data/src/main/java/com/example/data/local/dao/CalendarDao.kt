package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {

    @Insert
    suspend fun insert(habit: CalendarEntity)

    @Update
    fun update(habit: CalendarEntity)

    @Delete
    suspend fun delete(habit: CalendarEntity)

    //Этот запрос только для вывода списка привычек на календаре.
    //Если исправлять getFromDate - Посыпятся другие вьюшки
    @Query("SELECT * FROM calendar_habits WHERE calendar_date LIKE :calendarDate")
    fun getFromDateForUIList(calendarDate: String): Flow<List<CalendarEntity>>

    @Query("SELECT * FROM calendar_habits WHERE calendar_date LIKE :calendarDate")
    suspend fun getFromDate(calendarDate: String): List<CalendarEntity>

    @Query("SELECT habit_name FROM habits WHERE habitId = :id")
    suspend fun getNameById(id: Long): String

    @Query("SELECT habit_unit FROM habits WHERE habitId = :id")
    suspend fun getUnitById(id: Long): String

    @Query("SELECT habit_image FROM habits WHERE habitId = :id")
    suspend fun getImageById(id: Long): Int

    @Query("SELECT SUM(calendar_habit_price) FROM calendar_habits WHERE calendar_habit_id = :id AND calendar_date LIKE :calendarDate")
    suspend fun getSumPrice(id: Long, calendarDate: String): Float

    @Query("SELECT COUNT(DISTINCT calendar_date) FROM calendar_habits WHERE calendar_habit_id = :id AND calendar_date LIKE :calendarDate")
    suspend fun getSumDate(id: Long, calendarDate: String): Int

    @Query("SELECT SUM(calendar_habit_quantity) FROM calendar_habits WHERE calendar_habit_id = :id AND calendar_date LIKE :calendarDate")
    suspend fun getSumQuantity(id: Long, calendarDate: String): Float

    @Query("""
    SELECT * FROM calendar_habits WHERE calendar_habit_id = :id AND calendar_date LIKE :calendarDate
    ORDER BY calendar_date ASC, calendarId ASC""")
    suspend fun getHabitsForMonthFromId(
        id: Int,
        calendarDate: String
    ): List<CalendarEntity>
}