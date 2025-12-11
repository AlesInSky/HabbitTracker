package com.example.habbittracker.presentation.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.habbittracker.presentation.HabitActivity
import androidx.compose.ui.platform.LocalContext
import com.example.habbittracker.presentation.MainActivity

@Composable
fun BottomNav(currentActivity: Activity) {
    val context = LocalContext.current

    val listItem = listOf(
        NavItem.CalendarNav,
        NavItem.HabitNav
    )
    NavigationBar() {
        //val backStackEntry by navController.currentBackStackEntryAsState()
        //val currentRoute = backStackEntry?.destination?.route
        listItem.forEach { item ->
            val selected = when (item) {
                NavItem.CalendarNav -> currentActivity is MainActivity
                NavItem.HabitNav -> currentActivity is HabitActivity
            }
            NavigationBarItem(
                selected = selected,
                onClick = {
                    when (item) {
                        NavItem.CalendarNav ->
                            if (currentActivity !is MainActivity) {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as? Activity)?.finish()
                            }

                        NavItem.HabitNav ->
                            if (currentActivity !is HabitActivity) {
                                val intent = Intent(context, HabitActivity::class.java)
                                context.startActivity(intent)
                                (context as? Activity)?.finish()
                            }
                    }
                },
                icon = {
                    Icon(painter = painterResource(item.iconId), contentDescription = "Icon")
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.DarkGray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}