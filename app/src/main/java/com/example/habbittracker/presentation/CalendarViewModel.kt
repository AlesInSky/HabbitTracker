package com.example.habbittracker.presentation

import androidx.lifecycle.ViewModel
import com.example.domain.models.CalendarDateDomain
import com.example.domain.usecase.GetMonthUseCase
import com.example.domain.usecase.NextMonthUseCase
import com.example.domain.usecase.PreviousMonthUseCase
import java.util.Calendar

class CalendarViewModel(
    private val getMonthUseCase: GetMonthUseCase,
    private val nextMonthUseCase: NextMonthUseCase,
    private val previousMonthUseCase: PreviousMonthUseCase,
) : ViewModel() {


    class Days(val number: Int?)

    fun getMonth(){
       // getMonthUseCase.getMonthName()
    }

//    fun getMonth(month: Int): String {
//        val monthList = listOf(
//            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
//            "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
//        )
//        val monthTextView = monthList[month]
//        return monthTextView
//    }

//    fun nextMonth(): CalendarDateDomain {
//        val result = nextMonthUseCase.execute(month, year)
//        return result
//    }
//
//    fun previousMonth(): Int {
//        month--
//        if (month < 0) {
//            month = 11
//            year--
//        }
//        return month
//    }
//
//    fun getDayInMonth(): ArrayList<Days> {
//        calendar.set(Calendar.YEAR, year)
//        calendar.set(Calendar.MONTH, month)
//        calendar.set(Calendar.DAY_OF_MONTH, month)
//        val days = mutableListOf<Days>()
//
//        var dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//        var x = 1
//
//        while (x <= dayInMonth) {
//            days.add(Days(number = x))
//            x++
//        }
//        return days as ArrayList<Days>
//    }
}