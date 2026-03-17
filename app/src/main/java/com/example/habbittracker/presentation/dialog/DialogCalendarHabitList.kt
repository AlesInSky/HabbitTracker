package com.example.habbittracker.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DialogCalendarHabitList(
    onDismissRequest: () -> Unit,
    vm: CalendarViewModel,
    date: CalendarDateDomain
) {
    val habitList by vm.habitListFromDate.collectAsState()
    val vm: CalendarViewModel = koinViewModel()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
    habit: Map<String, Any?>,
    vm: CalendarViewModel = koinViewModel(),
) {
    Card(
        onClick = { /* Переход к деталям */ },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка привычки
            Icon(
                painter = painterResource(
                    id = habit["habit_image"] as? Int ?: R.drawable.card_add_icon
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
                Text(
                    text = habit["habit_name"] as? String ?: "Без названия",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Количество: ${habit["calendar_habit_quantity"]} ${habit["habit_unit"] ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                habit["habit_price"]?.let { price ->
                    if (price != 0) {
                        Text(
                            text = "Цена: $price",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Редактирование привычки
            IconButton(onClick =
                { val calendarHabit = CalendarEntity(
                    calendarId = (habit["calendarId"] as? Long) ?: 0L,
                    calendarHabitId = (habit["calendar_habit_id"] as? Long) ?: 0L,
                    calendarHabitPrice = (habit["calendar_habit_price"] as? Int) ?: 0,
                    calendarHabitQuantity = (habit["calendar_habit_quantity"] as? Float) ?: 0f,
                    calendarHabitDescription = (habit["calendar_habit_description"] as? String) ?: "",
                    calendarDate = (habit["calendar_date"] as? String) ?: ""
                )
                    vm.editHabit(calendarHabit) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_icon),
                    contentDescription = "Edit",
                    tint = Color.Unspecified
                )
            }

            // Удаление привычки
            IconButton(onClick =
                { val calendarHabit = CalendarEntity(
                    calendarId = (habit["calendarId"] as? Long) ?: 0L,
                    calendarHabitId = (habit["calendar_habit_id"] as? Long) ?: 0L,
                    calendarHabitPrice = (habit["calendar_habit_price"] as? Int) ?: 0,
                    calendarHabitQuantity = (habit["calendar_habit_quantity"] as? Float) ?: 0f,
                    calendarHabitDescription = (habit["calendar_habit_description"] as? String) ?: "",
                    calendarDate = (habit["calendar_date"] as? String) ?: ""
                )
                    vm.deleteHabit(calendarHabit) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "Delete",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun EditHabit(
    date: CalendarDateDomain
){

}

@Composable
fun AddHabitCard(
    date: CalendarDateDomain
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
        DialogNewCalendarHabit(
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