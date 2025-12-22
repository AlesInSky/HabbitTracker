package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    var habitId: Long = 0L,
    @ColumnInfo(name = "habit_name")
    var habitName: String = "",
    @ColumnInfo(name = "habit_unit")
    var habitUnit: String = "",
    @ColumnInfo(name = "habit_quantity")
    var habitQuantity: Int = 0,
    @ColumnInfo(name = "habit_description")
    var habitDescription: String = "",
    @ColumnInfo(name = "habit_image")
    var habitImage: Int? = null
)