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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.domain.models.HabitForDay
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel

private val EditHabitBlue = Color(0xFF5798D5)
private val EditHabitBlueLight = Color(0xFFEAF4FC)
private val EditHabitText = Color(0xFF263747)
private val EditHabitSecondary = Color(0xFF94A3B1)
private val EditHabitDivider = Color(0xFFEDF1F5)
private val EditHabitBorder = Color(0xFFEAF0F5)
private val EditHabitBackground = Color.White

// Диалог редактирования привычки за конкретный день
@Composable
fun DialogCalendarDayEditHabit(
    habit: HabitForDay,
    onDismissRequest: () -> Unit,
    vm: CalendarViewModel,
) {
    // Локальные состояния для ввода
    var priceText by remember {
        mutableStateOf(
            if (habit.calendarHabitPrice.toInt().toFloat() == habit.calendarHabitPrice) {
                habit.calendarHabitPrice.toInt().toString()
            } else {
                habit.calendarHabitPrice.toString()
            }
        )
    }

    var quantityText by remember {
        mutableStateOf(
            if (habit.calendarHabitQuantity.toInt().toFloat() == habit.calendarHabitQuantity) {
                habit.calendarHabitQuantity.toInt().toString()
            } else {
                habit.calendarHabitQuantity.toString()
            }
        )
    }

    var descriptionText by remember {
        mutableStateOf(habit.calendarHabitDescription ?: "")
    }

    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = EditHabitBackground
            ),
            border = BorderStroke(1.dp, EditHabitBorder),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Заголовок
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Редактирование записи",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = EditHabitText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = habit.calendarDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EditHabitSecondary
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = EditHabitDivider
                )

                // Карточка привычки
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = EditHabitBackground
                    ),
                    border = BorderStroke(1.dp, EditHabitBorder),
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
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = EditHabitBlueLight
                            )
                        ) {
                            Icon(
                                painter = painterResource(
                                    habit.habitImage ?: R.drawable.card_add_icon
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(7.dp)
                                    .size(40.dp),
                                tint = Color.Unspecified
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = habit.habitName ?: "Привычка",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = EditHabitText
                            )

                            Text(
                                text = "Запись за выбранный день",
                                style = MaterialTheme.typography.bodySmall,
                                color = EditHabitSecondary
                            )
                        }
                    }
                }

                // Количество
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Количество",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitText
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
                        shape = RoundedCornerShape(15.dp),
                        suffix = {
                            Text(
                                text = habit.habitUnit ?: "шт.",
                                color = EditHabitSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditHabitBlue,
                            unfocusedBorderColor = EditHabitBorder,
                            focusedLabelColor = EditHabitBlue,
                            cursorColor = EditHabitBlue,
                            focusedTextColor = EditHabitText,
                            unfocusedTextColor = EditHabitText,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                }

                // Стоимость
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Стоимость",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitText
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
                        shape = RoundedCornerShape(15.dp),
                        suffix = {
                            Text(
                                text = "₽",
                                color = EditHabitSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditHabitBlue,
                            unfocusedBorderColor = EditHabitBorder,
                            focusedLabelColor = EditHabitBlue,
                            cursorColor = EditHabitBlue,
                            focusedTextColor = EditHabitText,
                            unfocusedTextColor = EditHabitText,
                            errorBorderColor = MaterialTheme.colorScheme.error
                        )
                    )
                }

                // Комментарий
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Комментарий",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitText
                    )

                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = {
                            descriptionText = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 2,
                        shape = RoundedCornerShape(15.dp),
                        placeholder = {
                            Text(
                                text = "Добавьте комментарий",
                                color = EditHabitSecondary
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditHabitBlue,
                            unfocusedBorderColor = EditHabitBorder,
                            focusedLabelColor = EditHabitBlue,
                            cursorColor = EditHabitBlue,
                            focusedTextColor = EditHabitText,
                            unfocusedTextColor = EditHabitText
                        )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = EditHabitSecondary
                        ),
                        border = BorderStroke(
                            1.dp,
                            EditHabitBorder
                        )
                    ) {
                        Text(
                            text = "Отмена",
                            maxLines = 1
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
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
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EditHabitBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Сохранить",
                            maxLines = 1,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}