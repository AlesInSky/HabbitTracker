package com.example.habbittracker.presentation.dialog.dialogCalendar

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Цвета

private val HabitFormBlue = Color(0xFF5798D5)
private val HabitFormBlueDark = Color(0xFF367DBD)
private val HabitFormBlueLight = Color(0xFFEAF4FC)
private val HabitFormText = Color(0xFF263747)
private val HabitFormSecondary = Color(0xFF94A3B1)
private val HabitFormDivider = Color(0xFFEDF1F5)
private val HabitFormBorder = Color(0xFFEAF0F5)
private val HabitFormBackground = Color.White

// Формат даты

fun formatDateNewHabit(dateString: String): String {
    val date = LocalDate.parse(
        dateString,
        DateTimeFormatter.ofPattern("yyyy-M-d")
    )

    val months = listOf(
        "янв", "фев", "мар", "апр",
        "мая", "июн", "июл", "авг",
        "сен", "окт", "ноя", "дек"
    )

    return "${date.dayOfMonth} ${months[date.monthValue - 1]}"
}

// Диалог добавления привычки в дату календаря

@Composable
fun DialogCalendarDayNewHabit(
    date: CalendarDateDomain,
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (CalendarEntity) -> Unit,
    vm: CalendarViewModel,
) {
    if (habit == null) return

    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<HabitEntity?>(null) }

    val habitLoad by vm.habitList.collectAsState()
    val availableHabits = habitLoad.filter { !it.isDeleted }

    var description by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }

    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    //Дата

    val dateYear = date.year
    val dateMonth = date.month
    val dateDay = vm.currentMonthYear.collectAsState().value.date
    val getDate = "${dateYear}-${dateMonth}-${dateDay}"

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, HabitFormBorder),
            colors = CardDefaults.cardColors(
                containerColor = HabitFormBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Заголовок

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Новая привычка",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = HabitFormText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = formatDateNewHabit(getDate),
                        fontSize = 13.sp,
                        color = HabitFormSecondary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                HorizontalDivider(
                    color = HabitFormDivider,
                    thickness = 1.dp
                )

                //Выбор привычки

                Text(
                    text = "Привычка",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HabitFormText
                )

                Box {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expanded = true
                            },
                        shape = RoundedCornerShape(17.dp),
                        border = BorderStroke(1.dp, HabitFormBorder),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
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
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(HabitFormBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = selectedUnit?.habitImage
                                            ?: R.drawable.card_add_icon
                                    ),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = selectedUnit?.habitName
                                    ?: "Выберите привычку",
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (selectedUnit == null) {
                                    HabitFormSecondary
                                } else {
                                    HabitFormText
                                }
                            )

                            Icon(
                                painter = painterResource(
                                    android.R.drawable.arrow_down_float
                                ),
                                contentDescription = null,
                                tint = HabitFormSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Выпадающий список

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .heightIn(max = 275.dp),
                        shape = RoundedCornerShape(18.dp),
                        containerColor = Color.White,
                        tonalElevation = 2.dp,
                        shadowElevation = 4.dp
                    ) {
                        availableHabits.forEachIndexed { index, item ->

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                item.habitImage
                                                    ?: R.drawable.card_add_icon
                                            ),
                                            contentDescription = null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(30.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(12.dp)
                                        )

                                        Column {
                                            Text(
                                                text = item.habitName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = HabitFormText
                                            )

                                            Text(
                                                text = item.habitUnit,
                                                fontSize = 12.sp,
                                                color = HabitFormSecondary
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    selectedUnit = item
                                    priceText = item.habitPrice.toString()
                                    expanded = false
                                }
                            )

                            // Разделитель между привычками

                            if (index < availableHabits.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp
                                    ),
                                    thickness = 1.dp,
                                    color = HabitFormDivider
                                )
                            }
                        }
                    }
                }

                //Количество

                Text(
                    text = "Количество",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HabitFormText
                )

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { newText ->
                        if (
                            newText.all {
                                it.isDigit() || it == '.'
                            } || newText.isEmpty()
                        ) {
                            if (newText.count { it == '.' } <= 1) {
                                quantityText = newText
                                quantityError = false
                            } else {
                                quantityError = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = quantityError,
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text(
                            text = "Введите количество",
                            color = HabitFormSecondary,
                            fontSize = 13.sp
                        )
                    },
                    suffix = {
                        Text(
                            text = selectedUnit?.habitUnit ?: "-",
                            fontSize = 12.sp,
                            color = HabitFormBlueDark,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HabitFormBlue,
                        unfocusedBorderColor = HabitFormBorder,
                        focusedLabelColor = HabitFormBlue,
                        cursorColor = HabitFormBlue,
                        focusedTextColor = HabitFormText,
                        unfocusedTextColor = HabitFormText,
                        errorBorderColor = Color(0xFFE57373)
                    )
                )

                //Стоимость

                Text(
                    text = "Стоимость",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HabitFormText
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { newText ->
                        if (
                            newText.all {
                                it.isDigit() || it == '.'
                            } || newText.isEmpty()
                        ) {
                            if (newText.count { it == '.' } <= 1) {
                                priceText = newText
                                priceError = false
                            } else {
                                priceError = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = priceError,
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text(
                            text = "0",
                            color = HabitFormSecondary,
                            fontSize = 13.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HabitFormBlue,
                        unfocusedBorderColor = HabitFormBorder,
                        focusedLabelColor = HabitFormBlue,
                        cursorColor = HabitFormBlue,
                        focusedTextColor = HabitFormText,
                        unfocusedTextColor = HabitFormText,
                        errorBorderColor = Color(0xFFE57373)
                    )
                )

                //Комментарий

                Text(
                    text = "Комментарий",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HabitFormText
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 2,
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text(
                            text = "Добавьте комментарий",
                            color = HabitFormSecondary,
                            fontSize = 13.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HabitFormBlue,
                        unfocusedBorderColor = HabitFormBorder,
                        focusedLabelColor = HabitFormBlue,
                        cursorColor = HabitFormBlue,
                        focusedTextColor = HabitFormText,
                        unfocusedTextColor = HabitFormText
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                //Кнопки действий

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
                            contentColor = HabitFormBlueDark
                        ),
                        border = BorderStroke(
                            1.dp,
                            HabitFormBorder
                        )
                    ) {
                        Text(
                            text = "Отмена",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = {
                            if (selectedUnit?.habitName.isNullOrEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Выберите привычку",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            } else if (quantityText.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Введите количество",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            } else if (priceText.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Введите стоимость",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val updatedCalendarHabit = CalendarEntity(
                                calendarHabitId = selectedUnit?.habitId ?: -1,
                                calendarHabitPrice = priceText.toFloat(),
                                calendarHabitQuantity = quantityText.toFloat(),
                                calendarHabitDescription = description,
                                calendarDate = getDate
                            )

                            onSave(updatedCalendarHabit)
                            vm.getHabitListForMonth()
                            vm.updateCalendar()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HabitFormBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Сохранить",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
