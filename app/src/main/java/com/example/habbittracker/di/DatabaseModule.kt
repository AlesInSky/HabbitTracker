package com.example.habbittracker.di

import com.example.data.local.database.HabitAppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<HabitAppDatabase> {
        HabitAppDatabase.getInstance(androidContext())
    }

    single {
        get<HabitAppDatabase>().habitDao
    }

    single {
        get<HabitAppDatabase>().calendarDao
    }
}