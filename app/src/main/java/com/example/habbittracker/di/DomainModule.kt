package com.example.habbittracker.di

import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetMonthUseCase> {
        GetMonthUseCase(calendarRepository = get())
    }

    factory<NextMonthUseCase> {
        NextMonthUseCase(calendarRepository = get())
    }

    factory<PreviousMonthUseCase> {
        PreviousMonthUseCase(calendarRepository = get())
    }

}