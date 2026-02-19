package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.HabitDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity

@Database(entities = [HabitEntity::class, CalendarEntity::class], version = 3, exportSchema = false)
abstract class HabitAppDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val calendarDao: CalendarDao


    companion object {
        @Volatile
        private var INSTANCE: HabitAppDatabase? = null

        fun getInstance(context: Context): HabitAppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HabitAppDatabase::class.java,
                        "habit_app_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}