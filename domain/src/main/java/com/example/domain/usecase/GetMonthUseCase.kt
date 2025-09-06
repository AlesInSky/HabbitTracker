package com.example.domain.usecase

import com.example.domain.models.CalendarDateDomain
import com.example.domain.repository.CalendarRepository

//UseCase должен быть чистым: принимает параметры (если нужны) и возвращает результат, не хранит состояние.

//Текущее состояние месяца и года хранится во ViewModel.
//Форматирование для UI (getMonthName()) делается в ViewModel или Compose.

class GetMonthUseCase(private val calendarRepository: CalendarRepository) {

    fun execute(): CalendarDateDomain {
        return calendarRepository.getNowMonth()
    }

}