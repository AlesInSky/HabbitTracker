package com.example.habbittracker.presentation.dialog.dialogCalendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import com.example.habbittracker.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.domain.models.HabitForDay
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import com.example.habbittracker.ui.theme.ButtonCancelColor
import com.example.habbittracker.ui.theme.ButtonConfirmColor
import com.example.habbittracker.ui.theme.TextCardNumberColor


//Диалоговое окно при добавлении привычки в дату календаря
@Composable
fun DialogCalendarDayEditHabit(
    habit: HabitForDay,
    onDismissRequest: () -> Unit,
    vm: CalendarViewModel,
) {

    //Локальные состояния для ввода
    var priceText by remember {
        mutableStateOf(
            if (habit.calendarHabitPrice.toInt().toFloat() == habit.calendarHabitPrice)
                habit.calendarHabitPrice.toInt().toString()
            else habit.calendarHabitPrice.toString()
        )
    }
    var quantityText by remember {
        mutableStateOf(
            if (habit.calendarHabitQuantity.toInt().toFloat() == habit.calendarHabitQuantity)
                habit.calendarHabitQuantity.toInt().toString()
            else habit.calendarHabitQuantity.toString()
        )
    }
    var descriptionText by remember { mutableStateOf(habit.calendarHabitDescription ?: "") }

    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Заголовок
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Редактирование записи",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = habit.calendarDate,
                        color = Color.Gray
                    )
                }

                // Карточка привычки
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(
                                habit.habitImage
                                    ?: R.drawable.card_add_icon
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = habit.habitName ?: "Привычка",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                // Количество
                Text(
                    text = "Количество",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { newText ->
                        if (
                            newText.all { it.isDigit() || it == '.' } ||
                            newText.isEmpty()
                        ) {
                            if (newText.count { it == '.' } <= 1) {
                                quantityText = newText
                                quantityError = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = quantityError,
                    shape = RoundedCornerShape(24.dp),
                    suffix = {
                        Text(
                            text = habit.habitUnit ?: "шт.",
                            color = Color.Gray
                        )
                    }
                )

                // Стоимость
                Text(
                    text = "Стоимость",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { newText ->
                        if (
                            newText.all { it.isDigit() || it == '.' } ||
                            newText.isEmpty()
                        ) {
                            if (newText.count { it == '.' } <= 1) {
                                priceText = newText
                                priceError = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = priceError,
                    shape = RoundedCornerShape(24.dp),
                    suffix = {
                        Text(
                            text = "₽",
                            color = Color.Gray
                        )
                    }
                )

                // Комментарий
                Text(
                    text = "Комментарий",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = {
                        descriptionText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 2,
                    shape = RoundedCornerShape(24.dp),
                    placeholder = {
                        Text("Добавьте комментарий")
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

               // Spacer(modifier = Modifier.weight(1f))

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextCardNumberColor
                        ),
                        border = BorderStroke(
                            2.dp,
                            ButtonCancelColor
                        )
                    )
                    {
                        Text("Отмена", maxLines = 1)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {

                            var hasError = false

                            if (quantityText.isBlank()) {
                                quantityError = true
                                hasError = true
                            }

                            if (priceText.isBlank()) {
                                priceError = true
                                hasError = true
                            }

                            if (!hasError) {

                                vm.editHabit(
                                    id = habit.calendarId,
                                    habitId = habit.calendarHabitId,
                                    price = priceText.toFloat(),
                                    quantity = quantityText.toFloat(),
                                    description = descriptionText,
                                    date = habit.calendarDate
                                )

                                onDismissRequest()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonConfirmColor
                        )
                    ) {
                        Text("Сохранить", maxLines = 1)
                    }
                }
            }
        }
    }
}