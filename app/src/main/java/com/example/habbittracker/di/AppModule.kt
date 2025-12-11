package com.example.habbittracker.di

import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
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

}