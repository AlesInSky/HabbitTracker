package com.example.habbittracker.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.habbittracker.presentation.navigation.BottomNav
import com.example.habbittracker.ui.theme.HabbitTrackerTheme

class HabitActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabbitTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNav(currentActivity = this) }
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(
        text = "Habit screen !",
        modifier = Modifier
    )
}

