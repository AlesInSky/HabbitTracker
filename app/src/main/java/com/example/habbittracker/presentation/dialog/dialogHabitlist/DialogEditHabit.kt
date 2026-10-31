package com.example.habbittracker.presentation.dialog.dialogHabitlist

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R

private val EditHabitBlue = Color(0xFF5798D5)
private val EditHabitBlueDark = Color(0xFF367DBD)
private val EditHabitBlueLight = Color(0xFFEAF4FC)
private val EditHabitText = Color(0xFF263747)
private val EditHabitSecondary = Color(0xFF94A3B1)
private val EditHabitDivider = Color(0xFFEDF1F5)
private val EditHabitBorder = Color(0xFFEAF0F5)
private val EditHabitBackground = Color.White

// Диалоговое окно при редактировании привычки
@Composable
fun DialogEditHabit(
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (HabitEntity) -> Unit,
) {
    // Если нет привычки для редактирования — не показываем диалог
    if (habit == null) return

    // Локальные состояния
    var name by remember {
        mutableStateOf(habit.habitName)
    }

    var price by remember {
        mutableStateOf(habit.habitPrice.toString())
    }

    var selectedIcon by remember {
        mutableIntStateOf(
            habit.habitImage ?: R.drawable.card_add_icon
        )
    }

    var selectedImage by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    // Список единиц измерения
    val unitList = stringArrayResource(R.array.unit)

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedUnit by remember {
        mutableStateOf(
            habit.habitUnit
                ?.takeIf { it in unitList }
                ?: unitList.first()
        )
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = EditHabitBackground
            ),
            border = BorderStroke(
                1.dp,
                EditHabitBorder
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Заголовок
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Редактирование",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = EditHabitText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Измените параметры привычки",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EditHabitSecondary
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = EditHabitDivider
                )

                // Выбор иконки
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(
                                color = EditHabitBlueLight,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                selectedImage = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(selectedIcon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Изменить иконку",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitBlue,
                        modifier = Modifier.clickable {
                            selectedImage = true
                        }
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

                // Название привычки
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Название",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitText
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Например: Сигареты",
                                color = EditHabitSecondary
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditHabitBlue,
                            unfocusedBorderColor = EditHabitBorder,
                            cursorColor = EditHabitBlue,
                            focusedTextColor = EditHabitText,
                            unfocusedTextColor = EditHabitText
                        )
                    )
                }

                // Единица измерения
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Единица измерения",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = EditHabitText
                    )

                    Box {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expanded = true
                                },
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = EditHabitBackground
                            ),
                            border = BorderStroke(
                                1.dp,
                                EditHabitBorder
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedUnit,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = EditHabitText
                                )

                                Text(
                                    text = "⌄",
                                    color = EditHabitSecondary,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            },
                            modifier = Modifier
                                .background(EditHabitBackground)
                                .heightIn(max = 275.dp)
                        ) {
                            unitList.forEachIndexed { index, unit ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = unit,
                                            color = if (selectedUnit == unit) {
                                                EditHabitBlueDark
                                            } else {
                                                EditHabitText
                                            },
                                            fontWeight = if (selectedUnit == unit) {
                                                FontWeight.SemiBold
                                            } else {
                                                FontWeight.Normal
                                            }
                                        )
                                    },
                                    onClick = {
                                        selectedUnit = unit
                                        expanded = false
                                    }
                                )

                                if (index < unitList.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp
                                        ),
                                        thickness = 1.dp,
                                        color = EditHabitDivider
                                    )
                                }
                            }
                        }
                    }
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
                        value = price,
                        onValueChange = { newValue ->
                            price = newValue.filter {
                                it.isDigit()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        suffix = {
                            Text(
                                text = "₽",
                                color = EditHabitSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditHabitBlue,
                            unfocusedBorderColor = EditHabitBorder,
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