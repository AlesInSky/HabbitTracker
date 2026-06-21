package com.example.habbittracker.presentation.dialog.dialogHabitlist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.example.habbittracker.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import com.example.habbittracker.ui.theme.ButtonConfirmColor

//Диалоговое окно при добавлении привычки в список привычек
@Composable
fun DialogNewHabit(
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (HabitEntity) -> Unit,
    vm: HabitViewModel,
) {

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var selectedIcon by remember {
        mutableStateOf(
            habit?.habitImage ?: R.drawable.card_add_icon
        )
    }

    var selectedImage by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val unitList = stringArrayResource(R.array.unit)

    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(unitList.first()) }

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                //Шапка
                Text(
                    text = "Новая привычка",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                //Выбор иконки
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
                    }
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

                        Text(
                            text = selectedUnit
                        )

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
                    placeholder = {
                        Text("Введите стоимость", color = Color.Gray)
                    }
                )

                //---------------------------------
                // Кнопки
                //---------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismissRequest
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonConfirmColor
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

                            val newHabit = HabitEntity(
                                habitName = name,
                                habitUnit = selectedUnit,
                                habitPrice = price.toIntOrNull() ?: 0,
                                habitImage = selectedIcon
                            )

                            onSave(newHabit)
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}