package com.example.habbittracker.presentation.dialog

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel

//Диалоговое окно при добавлении привычки в дату календаря
@Composable
fun DialogNewCalendarHabit(
    date: CalendarDateDomain,
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (CalendarEntity) -> Unit,
    vm: CalendarViewModel,
) {
    // Если нет привычки для редактирования - не показываем диалог
    if (habit == null) return

    // Для DropDown Menu
    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<HabitEntity?>(null) }
    val habitLoad by vm.habitList.collectAsState()

    // Локальные состояния для редактирования
    val quantity = 0
    var description by remember { mutableStateOf(habit.habitDescription) }

    //Локальные состояния для ввода
    var priceText by remember { mutableStateOf("0") }
    var quantityText by remember { mutableStateOf("1") }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    //Дата
    val dateYear = date.year
    val dateMonth = date.month
    val dateDay = vm.currentDate.collectAsState().value.date
    val getDate = "${dateYear}-${dateMonth}-${dateDay}"

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)  // Больше места для содержимого
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
                    text = "Новая привычка",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Список иконок
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = selectedUnit?.habitImage ?: R.drawable.card_add_icon
                            ),
                            contentDescription = "Выбор иконки",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxSize()
                    ) { Text(text = selectedUnit?.habitName ?: "Выберите привычку") }

                    // Поле для названия
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        habitLoad.forEach { habit ->
                            DropdownMenuItem(
                                text = { Text(habit.habitName) },
                                onClick = {
                                    selectedUnit = habit
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Поле для ед.изм
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.width(104.dp)
                    )
                    { Text(selectedUnit?.habitUnit ?: "Ед.изм.") }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Поле для количества
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            quantityError = false
                        },
                        label = { Text("Количество") },
                        modifier = Modifier.weight(1f),
                        isError = quantityError,
                        singleLine = true
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Дата
                    Button(onClick = {}, enabled = false, modifier = Modifier.width(104.dp)) {
                        Text(getDate)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Поле для цены
                    // Надо реализовать изменение цены
                    OutlinedTextField(
                        value = selectedUnit?.habitPrice.toString(),
                        onValueChange = { newPriceText ->
                            selectedUnit?.habitPrice = newPriceText.toInt()
                            priceError = false
                        },
                        label = { Text("Стоимость") },
                        modifier = Modifier.weight(1f),
                        isError = priceError,
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
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            var hasError = false

                            if (selectedUnit?.habitName.isNullOrEmpty()) {
                                Toast.makeText(context, "Выберите привычку", Toast.LENGTH_SHORT)
                                    .show()
                                hasError = true
                            }
                            else if (quantityText.isBlank() && priceText.isBlank()) {
                                quantityError = true
                                Toast.makeText(context, "Введите количество", Toast.LENGTH_SHORT)
                                    .show()
                                hasError = true
                            }
                            else if (priceText.isBlank()) {
                                priceError = true
                                Toast.makeText(context, "Введите стоимость", Toast.LENGTH_SHORT)
                                    .show()
                                hasError = true
                            }
                            if (!hasError) {
                                // Создаем привычку на день недели
                                val updatedCalendarHabit = CalendarEntity(
                                    calendarHabitId = selectedUnit?.habitId ?: -1,
                                    calendarHabitPrice = selectedUnit?.habitPrice ?: -1,
                                    calendarHabitQuantity = quantity.toFloat(),
                                    calendarHabitDescription = description,
                                    calendarDate = getDate
                                )
                                Log.d("ADD", updatedCalendarHabit.toString())
                                onSave(updatedCalendarHabit)
                            }
                        }, modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}