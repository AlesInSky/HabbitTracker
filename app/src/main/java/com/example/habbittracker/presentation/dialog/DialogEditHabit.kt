package com.example.habbittracker.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
//Диалоговое окно при редактировании привычки
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditHabit(
    habit: HabitEntity?,
    onDismissRequest: () -> Unit,
    onSave: (HabitEntity) -> Unit
) {
    // Если нет привычки для редактирования - не показываем диалог
    if (habit == null) return

    // Локальные состояния для редактирования
    var name by remember { mutableStateOf(habit.habitName) }
    var description by remember { mutableStateOf(habit.habitDescription) }
    var selectedIcon by remember { mutableStateOf(habit.habitImage) }

    val unitsStub = listOf("шт", "кг", "л", "мин", "раз")

    var expanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(unitsStub.first()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)  // Больше места для содержимого
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
                        onClick = { /* Переход к деталям привычки */ }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.card_add_icon),
                            contentDescription = "Выбор иконки",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Поле для названия
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Название привычки") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedUnit,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("еи") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(PrimaryNotEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            unitsStub.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit) },
                                    onClick = {
                                        selectedUnit = unit
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
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
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            // Создаем обновленную привычку
                            val updatedHabit = habit.copy(
                                habitName = name,
                                habitDescription = description,
                                habitImage = selectedIcon
                            )
                            onSave(updatedHabit)
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

// Модель для иконки
data class HabitIcon(val iconRes: Int)

// Список доступных иконок
val availableHabitIcons = listOf(
    HabitIcon( R.drawable.card_add_icon)
)

// Компонент сетки иконок
@Composable
fun IconGrid(
    selectedIcon: Int?,
    onIconSelected: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(availableHabitIcons) { icon ->
            IconItem(
                icon = icon,
                isSelected = selectedIcon == icon.iconRes,
                onClick = { onIconSelected(icon.iconRes) }
            )
        }
    }
}

// Отдельная иконка для выбора
@Composable
fun IconItem(
    icon: HabitIcon,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon.iconRes),
            contentDescription = "",
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )
    }
}