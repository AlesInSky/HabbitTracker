package com.example.habbittracker.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel

@Composable
fun DialogNewHabit(
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (HabitEntity) -> Unit,
    vm: CalendarViewModel
) {
    // Если нет привычки для редактирования - не показываем диалог
    if (habit == null) return

    // Локальные состояния для редактирования
    var name by remember { mutableStateOf(habit.habitName) }
    var description by remember { mutableStateOf(habit.habitDescription) }
    var selectedIcon by remember { mutableStateOf(habit.habitImage) }

    val unitsStub = listOf("шт", "кг", "л", "мин", "раз")

    // Для DropDown Menu
    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<HabitEntity?>(null) }
    val habitLoad by vm.habitList.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)  // Больше места для содержимого
                .padding(10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Заголовок
                Text(
                    text = "Редактировать привычку",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Список иконок
                    IconButton(
                        onClick = { /* Переход к деталям привычки */ }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.card_add_icon),
                            contentDescription = "Выбор иконки",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Button(onClick = { expanded = true }) {
                        Text(text = selectedUnit?.habitName?: "Выберите привычку")
                    }

                    // Поле для названия
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ){
                        habitLoad.forEach { habit ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(habit.habitName) },
                                onClick = {
                                    selectedUnit = habit
                                    expanded = false
                                    name = habit.habitName
                                })
                        }
                    }
                }

                    // Поле для комментария
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Комментарий") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    // Кнопки действий
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Отмена")
                        }

                        Button(
                            onClick = {
                                // Создаем обновленную привычку
                                val updatedHabit = habit.copy(
                                    habitName = name,
                                    habitDescription = description,
                                    habitImage = selectedIcon
                                )
                                onSave(updatedHabit)
                            }
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }
    }
