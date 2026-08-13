package com.example.habbittracker.presentation.dialog.dialogHabitlist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.habbittracker.R
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.example.habbittracker.ui.theme.HabitDayColor

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
    var price by remember { mutableStateOf(habit.habitPrice.toString()) }
    var selectedIcon by remember { mutableIntStateOf(habit.habitImage ?: R.drawable.card_add_icon) }
    var selectedImage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Для dropDownMenu
    val unitList = stringArrayResource(R.array.unit)
    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(unitList.first()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Редактировать привычку",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                //Иконка
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    IconButton(
                        onClick = {
                            selectedImage = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(selectedIcon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Text(
                        text = "Изменить иконку",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                if (selectedImage) {
                    DialogImageSelect(
                        onSave = { icon ->
                            selectedIcon = icon
                            selectedImage = false
                        },
                        onDismiss = {
                            selectedImage = false
                        }
                    )
                }

                //Название привычки
                Text(
                    text = "Название",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    placeholder = {
                        Text("Например: Сигареты", color = Color.Gray)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                Text(
                    text = "Единица измерения",
                    style = MaterialTheme.typography.titleMedium
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = true
                        },
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(selectedUnit)

                        Text("▼")
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    unitList.forEach { unit ->

                        DropdownMenuItem(
                            text = {
                                Text(unit)
                            },
                            onClick = {
                                selectedUnit = unit
                                expanded = false
                            }
                        )
                    }
                }

                Text(
                    text = "Стоимость",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { newValue ->
                        price = newValue.filter {
                            it.isDigit()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                //Кнопки действия
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismissRequest
                    ) {
                        Text("Отмена", maxLines = 1)
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HabitDayColor
                        ),
                        onClick = {

                            if (name.isBlank()) {

                                Toast.makeText(
                                    context,
                                    "Введите название привычки",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }

                            val updatedHabit = habit.copy(
                                habitName = name,
                                habitUnit = selectedUnit,
                                habitPrice = price.toIntOrNull() ?: 0,
                                habitImage = selectedIcon
                            )

                            onSave(updatedHabit)
                        }
                    ) {
                        Text("Сохранить", maxLines = 1)
                    }
                }
            }
        }
    }
}