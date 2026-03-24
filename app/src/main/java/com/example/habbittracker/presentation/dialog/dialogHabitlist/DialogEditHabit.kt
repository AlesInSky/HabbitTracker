package com.example.habbittracker.presentation.dialog.dialogHabitlist

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.HabitEntity
import androidx.compose.material3.OutlinedTextField
import com.example.habbittracker.R

//Диалоговое окно при редактировании привычки
@Composable
fun DialogEditHabit(
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (HabitEntity) -> Unit,
) {

    // Если нет привычки для редактирования - не показываем диалог
    if (habit == null) return

    // Локальные состояния для редактирования
    var name by remember { mutableStateOf(habit.habitName) }
    var description by remember { mutableStateOf(habit.habitDescription) }
    var descriptionError by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf(habit.habitPrice.toString()) }
    var selectedIcon by remember { mutableIntStateOf(habit.habitImage ?: R.drawable.card_add_icon) }
    var selectedImage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Для dropDownMenu
    var unitItem = ""
    val unitList = stringArrayResource(R.array.unit)
    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(unitList.first()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)  // Больше места для содержимого
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
                        onClick = { selectedImage = true }
                    ) {
                        Icon(
                            painter = painterResource(id = selectedIcon),
                            contentDescription = "Выбор иконки",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            descriptionError = false
                        },
                        label = { Text("Наименование") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = descriptionError,
                        minLines = 1
                    )
                }

                if (selectedImage) {
                    DialogImageSelect(
                        onSave = { icon ->
                            selectedIcon = icon
                        },
                        onDismiss = { selectedImage = false })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Поле для ед.изм.
                    Button(onClick = { expanded = true }, modifier = Modifier.width(96.dp)) {
                        Text(selectedUnit)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        unitList.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    selectedUnit = unit
                                    expanded = false
                                    unitItem = unit
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Поле для цены
                    OutlinedTextField(
                        value = price,
                        onValueChange = { newValue ->
                            price = newValue.filter { it.isDigit() }
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Стоимость") },
                        singleLine = true
                    )
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
                    TextButton(onClick = onDismissRequest) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                descriptionError = true
                                Toast.makeText(
                                    context,
                                    "Введите название привычки",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val updatedHabit = habit.copy(
                                    habitName = name,
                                    habitUnit = unitItem,
                                    habitDescription = description,
                                    habitImage = selectedIcon,
                                    habitPrice = price.toIntOrNull() ?: 0,
                                )
                                onSave(updatedHabit)
                            }
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}