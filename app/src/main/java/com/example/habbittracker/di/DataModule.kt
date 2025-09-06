package com.example.habbittracker.di

import com.example.data.repository.CalendarRepositoryImpl
import com.example.data.storage.CalendarStorage
import com.example.data.storage.sharedprefs.SharedPrefsCalendarStorage
import com.example.domain.repository.CalendarRepository
import org.koin.dsl.module

val dataModule = module {

    single<CalendarStorage> {
        SharedPrefsCalendarStorage(context = get())
    }

    single<CalendarRepository> {
        CalendarRepositoryImpl(
            calendarStorage = get()
        )
    }

}