package com.example.habbittracker.presentation

import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.HabitEntity
import androidx.navigation.NavController
import com.example.habbittracker.R
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarDayHabitList
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarDayNewHabit
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarHabitInfo
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.habbittracker.ui.theme.*

@Composable
fun CalendarScreen(
    navController: NavController,
) {
    val vm: CalendarViewModel = koinViewModel()
    val monthName = stringArrayResource(R.array.months)
    val monthAndYear by vm.currentMonthYear.collectAsState()
    val habitInData by vm.habitListForMonth.collectAsState()
    val year = vm.getYear()
    val statisticMonthUI by vm.habitListForMonthUI.collectAsState()
    val calendarList by vm.calendarList.collectAsState()

    var showNewHabitDialog by remember { mutableStateOf(false) }
    var showHabitListDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                    onClick = {
                        vm.previousMoth()
                        vm.refresh()
                    },
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
                    text = monthName[monthAndYear.month] + " $year",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Button(
                    onClick = {
                        vm.nextMonth()
                        vm.refresh()
                    },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Пн", "Вт", "Ср", "Чт", "Пт").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                listOf("Сб", "Вс").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = WeekendColor,
                    )
                }
            }

            //Календарь (LazyVerticalGrid вместо RecyclerView)
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(4.dp)
            ) {

                items(calendarList) { day ->
                    if (day.isNotEmpty()) {
                        val dayInt = day.toInt()
                        val habitThisDate = dayInt in habitInData
                        Button(
                            onClick = {
                                vm.getDate(day.toInt())
                                if (habitThisDate) showHabitListDialog = true
                                else showNewHabitDialog = true
                            },
                            modifier = if (habitThisDate) {
                                Modifier
                                    .padding(4.dp)
                                    .size(48.dp)
                                    .border(
                                        width = 1.1.dp,
                                        color = HabitDayColor,
                                        shape = RoundedCornerShape(24.dp)
                                    )

                            } else Modifier
                                .padding(4.dp)
                                .size(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CardDayColorGray),

                            ) {
                            if (day.isNotEmpty()) {
                                Text(
                                    text = day,
                                    color = TextCardNumberColor,
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
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //Текст для вывода статистики
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = monthName[monthAndYear.month],
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            if (statisticMonthUI.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(R.drawable.card_add_icon),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "За этот месяц пока нет данных",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statisticMonthUI) { habit ->
                    HabitCard(
                        habit = habit
                    )
                }
            }
        }

        if (showHabitListDialog) {
            vm.loadHabitList()
            DialogCalendarDayHabitList(
                onDismissRequest = { showHabitListDialog = false },
                vm = vm,
                date = monthAndYear,
            )
            vm.getHabitListForMonth()
            vm.updateCalendar()
        }
        if (showNewHabitDialog) {
            showHabitListDialog = false
            DialogCalendarDayNewHabit(
                onDismissRequest = { showNewHabitDialog = false },
                date = monthAndYear,
                habit = HabitEntity(),
                vm = vm,
                onSave = { newHabit ->
                    vm.addHabit(newHabit)
                    showNewHabitDialog = false
                    vm.getInfoById()
                }
            )
        }
    }
}

@Composable
fun HabitCard(
    habit: CalendarViewModel.HabitUI,
) {
    var showHabitListDialog by remember { mutableStateOf(false) }

    Card(
        onClick = {
            showHabitListDialog = true
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = habit.image ?: R.drawable.card_add_icon
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(12.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = habit.name ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${habit.sumDay} дней",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Потрачено: ${habit.sumPrice.toInt()} ₽",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = habit.sumQuantity.toInt().toString(),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = habit.unit,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        if (showHabitListDialog) {
            DialogCalendarHabitInfo(
                onDismissRequest = {
                    showHabitListDialog = false
                },
                id = habit.id,
                image = habit.image
            )
        }
    }
}