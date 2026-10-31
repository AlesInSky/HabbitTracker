package com.example.habbittracker.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarDayHabitList
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarDayNewHabit
import com.example.habbittracker.presentation.dialog.dialogCalendar.DialogCalendarHabitInfo
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

// Цвета

private val CalendarBlue = Color(0xFF5798D5)
private val CalendarBlueDark = Color(0xFF367DBD)
private val CalendarBlueLight = Color(0xFFEAF4FC)
private val CalendarBlueBorder = Color(0xFFB7D8F2)

private val CalendarBackground = Color(0xFFFFFFFF)
private val CalendarText = Color(0xFF263747)
private val CalendarSecondaryText = Color(0xFF94A3B1)
private val CalendarDivider = Color(0xFFEDF1F5)

private val CalendarWeekend = Color(0xFF8AAAC5)

@Composable
fun CalendarScreen(
    navController: NavController,
) {
    val vm: CalendarViewModel = koinViewModel()

    val monthName = stringArrayResource(R.array.months)

    val monthAndYear by vm.currentMonthYear.collectAsState()
    val systemDate by vm.systemDate.collectAsState()
    val habitInData by vm.habitListForMonth.collectAsState()
    val statisticMonthUI by vm.habitListForMonthUI.collectAsState()
    val calendarList by vm.calendarList.collectAsState()

    var showNewHabitDialog by remember { mutableStateOf(false) }
    var showHabitListDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalendarBackground)
            .padding(horizontal = 18.dp)
    ) {

        // Верхняя панель с кнопками и месяцем

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Предыдущий месяц
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CalendarBlueLight)
                    .clickable {
                        vm.previousMoth()
                        vm.refresh()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "‹",
                    color = CalendarBlueDark,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = monthName[monthAndYear.month],
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CalendarText
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = vm.getYear().toString(),
                    fontSize = 13.sp,
                    color = CalendarSecondaryText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Следующий месяц
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CalendarBlueLight)
                    .clickable {
                        vm.nextMonth()
                        vm.refresh()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "›",
                    color = CalendarBlueDark,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        // Календарь

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFAFCFE))
                .border(
                    width = 1.dp,
                    color = CalendarDivider,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 14.dp
                )
        ) {

            // Шапка календаря. Дни недели

            val weekdays = listOf(
                "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weekdays.forEachIndexed { index, day ->

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (index >= 5) {
                                CalendarWeekend
                            } else {
                                CalendarSecondaryText
                            }
                        )
                    }
                }
            }

            // Календарные дни. Разбиваем существующий список на строки по 7 дней.

            calendarList.chunked(7).forEach { week ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    week.forEachIndexed { index, day ->

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(43.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            if (day.isNotEmpty()) {

                                val dayInt = day.toInt()
                                val habitThisDate = dayInt in habitInData

                                val isToday =
                                    day == systemDate.day.toString() &&
                                            monthAndYear.month == systemDate.month - 1 &&
                                            monthAndYear.year == systemDate.year

                                val dayBackground = when {
                                    isToday -> CalendarBlue
                                    habitThisDate -> CalendarBlueLight
                                    else -> Color.Transparent
                                }

                                val dayBorder = when {
                                    isToday -> CalendarBlue
                                    habitThisDate -> CalendarBlueBorder
                                    else -> Color.Transparent
                                }

                                Box(
                                    modifier = Modifier
                                        .size(39.dp)
                                        .clip(CircleShape)
                                        .background(dayBackground)
                                        .border(
                                            width = if (habitThisDate || isToday) 1.dp else 0.dp,
                                            color = dayBorder,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            vm.getDate(dayInt)

                                            if (habitThisDate) {
                                                showHabitListDialog = true
                                            } else {
                                                showNewHabitDialog = true
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = day,
                                        fontSize = 14.sp,
                                        fontWeight = when {
                                            isToday -> FontWeight.Bold
                                            habitThisDate -> FontWeight.SemiBold
                                            else -> FontWeight.Normal
                                        },
                                        color = when {
                                            isToday -> Color.White
                                            habitThisDate -> CalendarBlueDark
                                            index >= 5 -> CalendarWeekend
                                            else -> CalendarText
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Если последняя строка неполная
                    repeat(7 - week.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(43.dp)
                        )
                    }
                }
            }
        }

        // Окно статистики

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp, bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Статистика",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CalendarText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Твои результаты за месяц",
                        fontSize = 12.sp,
                        color = CalendarSecondaryText
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CalendarBlueLight)
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = monthName[monthAndYear.month],
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CalendarBlueDark
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        if (statisticMonthUI.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CalendarBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.card_add_icon),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = CalendarBlue
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Пока нет привычек",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CalendarText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Добавь привычку в календаре,\nчтобы здесь появилась статистика",
                    fontSize = 13.sp,
                    color = CalendarSecondaryText,
                    lineHeight = 19.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(statisticMonthUI) { habit ->
                    HabitCard(habit = habit)
                }
            }
        }

        // Диалоговые окна

        if (showHabitListDialog) {

            vm.loadHabitList()

            DialogCalendarDayHabitList(
                onDismissRequest = {
                    showHabitListDialog = false
                },
                vm = vm,
                date = monthAndYear
            )
            vm.getHabitListForMonth()
            vm.updateCalendar()
        }

        if (showNewHabitDialog) {
            showHabitListDialog = false
            DialogCalendarDayNewHabit(
                onDismissRequest = {
                    showNewHabitDialog = false
                },
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

// Карточка для привычки в статистику

@Composable
fun HabitCard(
    habit: CalendarViewModel.HabitUI,
) {
    var showHabitListDialog by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = {
            showHabitListDialog = true
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFEAF0F5)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            // Верхняя часть карточки

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = habit.image ?: R.drawable.card_add_icon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = habit.name ?: "Unknown",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CalendarText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${habit.sumDay} дней",
                        fontSize = 12.sp,
                        color = CalendarSecondaryText
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (
                            habit.sumQuantity.toInt().toFloat() == habit.sumQuantity
                        ) {
                            habit.sumQuantity.toInt().toString()
                        } else {
                            habit.sumQuantity.toString()
                        },
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = CalendarBlueDark
                    )

                    Text(
                        text = habit.unit,
                        fontSize = 11.sp,
                        color = CalendarSecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color(0xFFF0F3F6),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(11.dp))

            // Нижняя часть карточки

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Расходы за месяц",
                    fontSize = 12.sp,
                    color = CalendarSecondaryText
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${habit.sumPrice.toInt()} ₽",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CalendarText
                )
            }
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