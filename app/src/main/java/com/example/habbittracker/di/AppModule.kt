package com.example.habbittracker.di

import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<CalendarViewModel> {
        CalendarViewModel(
            getMonthUseCase = get(),
            nextMonthUseCase = get(),
            previousMonthUseCase = get()
        )
    }

    viewModel<HabitViewModel> {
        HabitViewModel(
            dao = get()
        )
    }
}