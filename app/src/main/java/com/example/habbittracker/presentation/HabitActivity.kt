package com.example.habbittracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
import com.example.habbittracker.presentation.dialog.DialogEditHabit
import com.example.habbittracker.presentation.navigation.BottomNav
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import com.example.habbittracker.ui.theme.HabbitTrackerTheme
import org.koin.androidx.compose.koinViewModel

class HabitActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabbitTrackerTheme {
                Scaffold(
                    bottomBar = { BottomNav(currentActivity = this) },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues)
                            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HabitList()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HabitList() {
    val vm: HabitViewModel = koinViewModel()
    val habitList by vm.habitList.collectAsState()
    val editHabit by vm.editHabit.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habitList) { habit ->
            HabitCard(habit = habit)
        }
        item {
            AddHabitCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (editHabit != null) {
        DialogEditHabit(
            habit = editHabit,
            onDismissRequest = { vm.closeEditDialog() },
            onSave = { updateHabit: HabitEntity ->
                vm.updateHabit(updateHabit) }
        )
    }
}


@Composable
fun HabitCard(habit: HabitEntity) {
    val vm: HabitViewModel = koinViewModel()

    Card(
        onClick = { /* Переход к деталям привычки */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = habit.habitName, modifier = Modifier.weight(1f))

            // Кнопка редактирования
            IconButton(onClick = { vm.openEditDialog(habit) }) {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit"
                )
            }
            // Кнопка удаления
            IconButton(onClick = { vm.deleteHabit(habit) }) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
fun AddHabitCard() {
    val vm: HabitViewModel = koinViewModel()

    Card(
        onClick = {
            vm.addHabit()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.card_add_icon),
                contentDescription = "Add"
            )
        }
    }
}

@Preview
@Composable
fun PreviewHabitScreen() {
    HabbitTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HabitCard(
                habit = HabitEntity(
                    habitId = 1,
                    habitName = "Пример привычки",
                    //habitDescription = "Описание"
                )
            )
            AddHabitCard()
        }
    }
}