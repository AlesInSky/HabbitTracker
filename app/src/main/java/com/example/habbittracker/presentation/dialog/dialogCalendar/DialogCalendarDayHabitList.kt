package com.example.habbittracker.presentation.dialog.dialogCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

private val DayHabitBlue = Color(0xFF5798D5)
private val DayHabitBlueDark = Color(0xFF367DBD)
private val DayHabitBlueLight = Color(0xFFEAF4FC)
private val DayHabitText = Color(0xFF263747)
private val DayHabitSecondary = Color(0xFF94A3B1)
private val DayHabitDivider = Color(0xFFEDF1F5)
private val DayHabitBorder = Color(0xFFEAF0F5)
private val DayHabitBackground = Color.White

// Диалог со списком привычек за выбранный день
@Composable
fun DialogCalendarDayHabitList(
    onDismissRequest: () -> Unit,
    vm: CalendarViewModel,
    date: CalendarDateDomain,
) {
    val habitList by vm.habitListForDay.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = DayHabitBackground
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                DayHabitBorder
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Привычки за день",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = DayHabitText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ваши записи за выбранную дату",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DayHabitSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = DayHabitDivider
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 460.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = habitList,
                        key = { it.calendarId }
                    ) { habit ->
                        HabitCard(
                            habit = habit,
                            vm = vm
                        )
                    }

                    item {
                        AddHabitCard(
                            date = date,
                            vm = vm
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitForDay,
    vm: CalendarViewModel = koinViewModel(),
) {
    var showEditHabitDialog by remember {
        mutableStateOf(false)
    }

    var selectedHabit by remember {
        mutableStateOf<HabitForDay?>(null)
    }

    val habitQuantityFloat = habit.calendarHabitQuantity
    val formattedQuantityToF =
        if (habitQuantityFloat == habitQuantityFloat.toInt().toFloat()) {
            habitQuantityFloat.toInt().toString()
        } else {
            habitQuantityFloat.toString()
        }

    val habitPriceFloat = habit.calendarHabitPrice
    val formattedPriceToF =
        if (habitPriceFloat == habitPriceFloat.toInt().toFloat()) {
            habitPriceFloat.toInt().toString()
        } else {
            habitPriceFloat.toString()
        }

    Card(
        onClick = {
            selectedHabit = habit
            showEditHabitDialog = true
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = DayHabitBackground
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            DayHabitBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка привычки
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = DayHabitBlueLight,
                        shape = RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = habit.habitImage ?: R.drawable.card_add_icon
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Информация о привычке
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = habit.habitName ?: "Без названия",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DayHabitText
                )

                Text(
                    text = "$formattedQuantityToF ${habit.habitUnit ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DayHabitSecondary
                )

                if (habit.calendarHabitPrice.toInt() != 0) {
                    Text(
                        text = "Цена: $formattedPriceToF",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DayHabitBlueDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Удаление привычки
            IconButton(
                onClick = {
                    val calendarHabit = CalendarEntity(
                        calendarId = habit.calendarId,
                        calendarHabitId = habit.calendarHabitId,
                        calendarHabitPrice = habit.calendarHabitPrice,
                        calendarHabitQuantity = habit.calendarHabitQuantity,
                        calendarHabitDescription =
                            habit.calendarHabitDescription ?: "",
                        calendarDate = habit.calendarDate
                    )

                    vm.deleteHabit(calendarHabit)
                    vm.getInfoById()
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_delete_black
                    ),
                    contentDescription = "Удалить привычку",
                    tint = DayHabitSecondary,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
    }

    if (showEditHabitDialog && selectedHabit != null) {
        DialogCalendarDayEditHabit(
            habit = selectedHabit!!,
            onDismissRequest = {
                showEditHabitDialog = false
                selectedHabit = null
            },
            vm = vm
        )
    }
}

@Composable
fun AddHabitCard(
    date: CalendarDateDomain,
    vm: CalendarViewModel = koinViewModel(),
) {
    var showNewHabitDialog by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = {
            showNewHabitDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = DayHabitBlueLight
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            DayHabitBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.card_add_icon
                ),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = DayHabitBlue
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Добавить привычку",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = DayHabitBlueDark
            )
        }
    }

    if (showNewHabitDialog) {
        DialogCalendarDayNewHabit(
            habit = HabitEntity(),
            onDismissRequest = {
                showNewHabitDialog = false
            },
            onSave = { newHabit ->
                vm.addHabit(newHabit)
                showNewHabitDialog = false
            },
            vm = vm,
            date = date
        )
    }
}