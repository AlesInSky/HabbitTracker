package com.example.habbittracker.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
import androidx.navigation.NavController
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarHabitList
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendar
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogNewCalendarHabit
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(
    navController: NavController
) {
    Log.d("TAG","Launch CalendarScreen")

    val vm: CalendarViewModel = koinViewModel()
    val monthName = stringArrayResource(R.array.months)
    val month by vm.currentMonth.collectAsState()
    val firstDayOfWeek by vm.currentMonth.collectAsState()
    val dayInMonth = vm.getDayInMonth()
    val habitInData by vm.habitListForDate.collectAsState()
    val year = vm.getYear()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showNewHabitDialog by remember { mutableStateOf(false) }
    var showHabitListDialog by remember { mutableStateOf(false) }

    val calendarCells = remember(firstDayOfWeek.firstDayOfWeek, dayInMonth) {
        val emptyCells: Int = when (firstDayOfWeek.firstDayOfWeek) {
            1 -> 6
            2 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5
            else -> 7
        }
        List(emptyCells) { "" } + (1..dayInMonth).map { it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        //Верхняя панель с кнопками и месяцем
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { vm.previousMoth() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    "←",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = monthName[month.month],
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = year.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Button(
                onClick = { vm.nextMonth() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    "→",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        //Дни недели
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Календарь (LazyVerticalGrid вместо RecyclerView)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 дней недели
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {

            items(calendarCells) { day ->
                if (day.isNotEmpty()) {
                    val dayInt = day.toInt()
                    val habitThisDate = dayInt in habitInData
                    Button(
                        onClick = {
                            vm.getDate(day.toInt())
                            if (habitThisDate) showHabitListDialog = true
                            else showConfirmDialog = true
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .size(48.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (habitThisDate) Color.Red else MaterialTheme.colorScheme.primary
                        )

                    ) {
                        if (day.isNotEmpty()) {
                            Text(
                                text = day,
                                fontSize = 14.sp
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(48.dp)
                            )
                        }
                    }
                }
            }
        }
        if (showConfirmDialog) {
            DialogCalendar(
                onDismissRequest = { showConfirmDialog = false },
                onConfirmation = {
                    // действие на Yes
                    showConfirmDialog = false
                    showNewHabitDialog = true
                }
            )
        }
        if (showNewHabitDialog) {
            showHabitListDialog = false
            DialogNewCalendarHabit(
                onDismissRequest = { showNewHabitDialog = false },
                date = month,
                habit = HabitEntity(),
                vm = vm,
                onSave = { newHabit ->
                    vm.addHabit(newHabit)
                    showNewHabitDialog = false
                }
            )
        }

        if (showHabitListDialog) {
            vm.loadHabitList()
            DialogCalendarHabitList(
                onDismissRequest = { showHabitListDialog = false },
                vm = vm,
                date = month,
            )
        }
    }
}