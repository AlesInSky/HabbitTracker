package com.example.habbittracker.presentation.navigation

import com.example.habbittracker.R

sealed class NavItem(val title: String, val iconId: Int, val route: String) {
    object CalendarNav: NavItem("Calendar",R.drawable.calendar_icon, "calendarRoute")
    object HabitNav: NavItem("Habit list",R.drawable.habit_icon, "habitRoute")
}