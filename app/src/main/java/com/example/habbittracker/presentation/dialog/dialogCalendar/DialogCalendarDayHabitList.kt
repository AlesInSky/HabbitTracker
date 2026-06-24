package com.example.habbittracker.presentation.dialog.dialogCalendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.example.habbittracker.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.domain.models.HabitForDay
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

//Диалоговое окно со списком привычек на конкретный день
@Composable
fun DialogCalendarDayHabitList(
    onDismissRequest: () -> Unit,
    vm: CalendarViewModel,
    date: CalendarDateDomain,
) {
    val habitList by vm.habitListForDay.collectAsState()
    val vm: CalendarViewModel = koinViewModel()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                //.height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Привычки за день",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(habitList) { habit ->
                        HabitCard(
                            habit = habit,
                            vm = vm
                        )
                    }
                    item {
                        AddHabitCard(
                            date = date
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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

    var showEditHabitDialog by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<HabitForDay?>(null) }

    val habitQuantityFloat = (habit.calendarHabitQuantity)
    val formattedQuantityToF =
        if (habitQuantityFloat == habitQuantityFloat.toInt().toFloat()) habitQuantityFloat.toInt()
            .toString() else habitQuantityFloat.toString()

    val habitPriceFloat = (habit.calendarHabitPrice)
    val formattedPriceToF =
        if (habitPriceFloat == habitPriceFloat.toInt().toFloat()) habitPriceFloat.toInt()
            .toString() else habitPriceFloat.toString()

    Card(
        onClick = {
            selectedHabit = habit
            showEditHabitDialog = true
        },
        modifier = Modifier.fillMaxWidth(),
    )

    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка привычки
            Icon(
                painter = painterResource(
                    id = habit.habitImage ?: R.drawable.card_add_icon
                ),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о привычке
            Column(
                modifier = Modifier.weight(1f)
            ) {
                //Наименование
                Text(
                    text = habit.habitName ?: "Без названия",
                    style = MaterialTheme.typography.titleMedium
                )
                //Количество
                Text(
                    text = "$formattedQuantityToF ${habit.habitUnit ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                //Цена
                habit.calendarHabitPrice.let { price ->
                    if (price.toInt() != 0) {
                        Text(
                            text = "Цена: $formattedPriceToF",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            // Удаление привычки
            IconButton(
                onClick =
                    {
                        val calendarHabit = CalendarEntity(
                            calendarId = (habit.calendarId),
                            calendarHabitId = (habit.calendarHabitId),
                            calendarHabitPrice = (habit.calendarHabitPrice),
                            calendarHabitQuantity = (habit.calendarHabitQuantity),
                            calendarHabitDescription = (habit.calendarHabitDescription ?: ""),
                            calendarDate = (habit.calendarDate)
                        )
                        vm.deleteHabit(calendarHabit)
                        vm.getInfoById()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_black),
                    contentDescription = "Delete",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun AddHabitCard(
    date: CalendarDateDomain,
) {
    val vm: CalendarViewModel = koinViewModel()
    var showNewHabitDialog by remember { mutableStateOf(false) }

    Card(
        onClick = {
            showNewHabitDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
    {
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
    if (showNewHabitDialog) {
        DialogCalendarDayNewHabit(
            habit = HabitEntity(),
            onDismissRequest = { showNewHabitDialog = false },
            onSave = { newHabit ->
                vm.addHabit(newHabit)
                showNewHabitDialog = false
            },
            vm = vm,
            date = date
        )
    }
}