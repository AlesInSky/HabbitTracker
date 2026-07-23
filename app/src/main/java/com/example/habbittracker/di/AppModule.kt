package com.example.habbittracker.di

import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import com.example.habbittracker.presentation.viewmodel.StatisticViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<CalendarViewModel> {
        CalendarViewModel(
            calendarDao = get(),
            habitDao = get(),
            calendarRepository = get()
        )
    }

    viewModel<HabitViewModel> {
        HabitViewModel(
            dao = get()
        )
    }

    viewModel<StatisticViewModel> {
        StatisticViewModel(
        )
    }
}