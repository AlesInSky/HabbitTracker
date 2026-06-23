package com.example.habbittracker.presentation.dialog.dialogCalendar

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.HabitEntity
import com.example.domain.models.CalendarDateDomain
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ButtonDefaults
import com.example.habbittracker.R
import androidx.compose.material3.OutlinedButton
import com.example.habbittracker.ui.theme.ButtonCancelColor
import com.example.habbittracker.ui.theme.ButtonConfirmColor
import com.example.habbittracker.ui.theme.DropdownCardColor
import com.example.habbittracker.ui.theme.TextCardNumberColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Диалоговое окно при добавлении привычки в дату календаря
@Composable
fun DialogCalendarDayNewHabit(
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
    var description by remember { mutableStateOf("") }

    //Локальные состояния для ввода
    val context = LocalContext.current

    var priceText by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }

    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    //Дата
    val dateYear = date.year
    val dateMonth = date.month
    val dateDay = vm.currentMonthYear.collectAsState().value.date
    val getDate = "${dateYear}-${dateMonth}-${dateDay}"

    fun formatDate(dateString: String): String {
        val date = LocalDate.parse(
            dateString,
            DateTimeFormatter.ofPattern("yyyy-M-d")
        )
        val months = listOf(
            "янв",
            "фев",
            "мар",
            "апр",
            "мая",
            "июн",
            "июл",
            "авг",
            "сен",
            "окт",
            "ноя",
            "дек"
        )
        return "${date.dayOfMonth} ${months[date.monthValue]}"
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Заголовок
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Новая привычка",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatDate(getDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }

                //Выбор привычки
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = true
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Unspecified
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(
                                id = selectedUnit?.habitImage ?: R.drawable.card_add_icon
                            ),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = selectedUnit?.habitName ?: "Выберите привычку",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            painter = painterResource(android.R.drawable.arrow_down_float),
                            contentDescription = null
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .heightIn(max = 275.dp)
                        ) {
                            habitLoad.forEach { habit ->
                                if (!habit.isDeleted) {
                                    DropdownMenuItem(
                                        text = {
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = DropdownCardColor
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(start = 12.dp, bottom = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    Icon(
                                                        painter = painterResource(
                                                            habit.habitImage
                                                                ?: R.drawable.card_add_icon
                                                        ),
                                                        contentDescription = null,
                                                        tint = Color.Unspecified,
                                                        modifier = Modifier.size(28.dp)
                                                    )

                                                    Spacer(modifier = Modifier.width(12.dp))

                                                    Column {

                                                        Text(
                                                            text = habit.habitName,
                                                            style = MaterialTheme.typography.bodyLarge
                                                        )

                                                        Text(
                                                            text = habit.habitUnit,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = Color.Gray
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        onClick = {
                                            selectedUnit = habit
                                            priceText = habit.habitPrice.toString()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                //Заголовок - Количество
                Text(
                    text = "Количество",
                    style = MaterialTheme.typography.titleMedium
                )
                //Ввод количества + отображение ед.изм.
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() || it == '.' } || newText.isEmpty()) {
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
                    shape = RoundedCornerShape(24.dp),

                    suffix = {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = selectedUnit?.habitUnit ?: "-",
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                            )
                        }
                    }
                )

                // Поле для цены
                Text(
                    text = "Стоимость",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() || it == '.' } || newText.isEmpty()) {
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
                        shape = RoundedCornerShape(24.dp),
                        placeholder = {
                            Text("0")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }

                // Поле для комментария
                Text(
                    text = "Комментарий",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 2,
                    shape = RoundedCornerShape(24.dp),
                    placeholder = {
                        Text("Добавьте комментарий")
                    }
                )

                // Кнопки действий
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            if (selectedUnit?.habitName.isNullOrEmpty()) {
                                Toast.makeText(context, "Выберите привычку", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            } else if (quantityText.isBlank()) {
                                Toast.makeText(context, "Введите количество", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            } else if (priceText.isBlank()) {
                                Toast.makeText(context, "Введите стоимость", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }

                            // Создаем привычку на день недели
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
