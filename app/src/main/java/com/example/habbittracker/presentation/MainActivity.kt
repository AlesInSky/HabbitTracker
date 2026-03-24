package com.example.habbittracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habbittracker.presentation.navigation.BottomNavHost
import com.example.habbittracker.presentation.navigation.Destination
import com.example.habbittracker.presentation.navigation.NavigationBarExample
import com.example.habbittracker.ui.theme.HabbitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabbitTrackerTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationBarExample()
                    }
                }
            }
        }
    }

//    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            HabbitTrackerTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    bottomBar = { BottomNavHost(
//                        navController = rememberNavController(),
//                        startDestination = Destination.CALENDAR,
//                        modifier = Modifier
//                    ) }
//                ) {
//                    MyNavigationApp()
//                }
//            }
//        }
//    }
//}

//@Composable
//fun MyNavigationApp() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "calendarScreen") {
//        composable("mainScreen") {
//            CalendarScreen(navController)
//        }
//        composable("detailScreen/{characterId}") {
//            HabitListScreen(navController)
//        }
//    }
//}