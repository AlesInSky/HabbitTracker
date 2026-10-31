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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.habbittracker.R
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.presentation.viewmodel.HabitViewModel

private val NewHabitBlue = Color(0xFF5798D5)
private val NewHabitBlueDark = Color(0xFF367DBD)
private val NewHabitBlueLight = Color(0xFFEAF4FC)
private val NewHabitText = Color(0xFF263747)
private val NewHabitSecondary = Color(0xFF94A3B1)
private val NewHabitDivider = Color(0xFFEDF1F5)
private val NewHabitBorder = Color(0xFFEAF0F5)
private val NewHabitBackground = Color.White

// Диалоговое окно при добавлении привычки в список привычек
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
    var selectedUnit by remember {
        mutableStateOf(unitList.first())
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = NewHabitBackground
            ),
            border = BorderStroke(1.dp, NewHabitBorder),
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
                        text = "Новая привычка",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NewHabitText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Создайте привычку и настройте её",
                        fontSize = 13.sp,
                        color = NewHabitSecondary
                    )
                }

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
                                color = NewHabitBlueLight,
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
                        color = NewHabitBlueDark,
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
                Text(
                    text = "Название",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NewHabitText
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Например: Сигареты",
                            color = NewHabitSecondary,
                            fontSize = 14.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NewHabitBlue,
                        unfocusedBorderColor = NewHabitBorder,
                        focusedLabelColor = NewHabitBlue,
                        cursorColor = NewHabitBlue,
                        focusedTextColor = NewHabitText,
                        unfocusedTextColor = NewHabitText
                    )
                )

                // Единица измерения
                Text(
                    text = "Единица измерения",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NewHabitText
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
                            containerColor = NewHabitBackground
                        ),
                        border = BorderStroke(1.dp, NewHabitBorder),
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
                                fontSize = 14.sp,
                                color = NewHabitText
                            )

                            Text(
                                text = "▼",
                                fontSize = 12.sp,
                                color = NewHabitSecondary
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier
                            .background(NewHabitBackground)
                    ) {
                        unitList.forEachIndexed { index, unit ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = unit,
                                        color = if (selectedUnit == unit) {
                                            NewHabitBlueDark
                                        } else {
                                            NewHabitText
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
                                    color = NewHabitDivider
                                )
                            }
                        }
                    }
                }

                // Стоимость
                Text(
                    text = "Стоимость",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NewHabitText
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
                    placeholder = {
                        Text(
                            text = "Введите стоимость",
                            color = NewHabitSecondary,
                            fontSize = 14.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NewHabitBlue,
                        unfocusedBorderColor = NewHabitBorder,
                        cursorColor = NewHabitBlue,
                        focusedTextColor = NewHabitText,
                        unfocusedTextColor = NewHabitText
                    )
                )

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
                            contentColor = NewHabitText
                        ),
                        border = BorderStroke(
                            1.dp,
                            NewHabitBorder
                        )
                    ) {
                        Text(
                            text = "Отмена",
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NewHabitBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
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
                        Text(
                            text = "Сохранить",
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}