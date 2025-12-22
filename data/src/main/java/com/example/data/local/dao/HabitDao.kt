package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.HabitEntity

@Dao
interface HabitDao {

    @Insert
    fun insert(habit: HabitEntity)

    @Update
    fun update(habit: HabitEntity)

    @Delete
    fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habits WHERE habitId = :habitId")
    fun get(habitId: Long): HabitEntity?

    @Query("SELECT * FROM habits ORDER BY habitId DESC")
    fun getAll(): List<HabitEntity>
}