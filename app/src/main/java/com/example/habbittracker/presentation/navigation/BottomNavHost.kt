package com.example.habbittracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.habbittracker.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habbittracker.presentation.CalendarScreen
import com.example.habbittracker.presentation.HabitListScreen

enum class Destination(
    val title: String, val iconId: Int, val route: String,
) {
    CALENDAR("Calendar", R.drawable.calendar_icon, "calendarRoute"),
    HABIT_LIST("Habit list", R.drawable.habit_icon, "habitRoute"),
    //STATISTICS("Statistics", R.drawable.card_add_icon,"statisticsRoute")
}

@Composable
fun BottomNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier,
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.CALENDAR -> CalendarScreen(navController)
                    Destination.HABIT_LIST -> HabitListScreen(navController)
                    //Destination.STATISTICS -> CalendarScreen(navController)
                }
            }
        }
    }
}

@Preview
@Composable
fun NavigationBarExample(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.CALENDAR
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.calendar_icon),
                                contentDescription = destination.title
                            )
                        },
                        label = { Text(destination.title) }
                    )
                }
            }
        }
    ) { contentPadding ->
        BottomNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
    }
}