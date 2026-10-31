package com.example.habbittracker.presentation.dialog.dialogCalendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.habbittracker.R
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Цвета

private val HabitInfoBlueDark = Color(0xFF367DBD)
private val HabitInfoText = Color(0xFF263747)
private val HabitInfoSecondary = Color(0xFF94A3B1)
private val HabitInfoDivider = Color(0xFFEDF1F5)
private val HabitInfoBorder = Color(0xFFEAF0F5)
private val HabitInfoBackground = Color.White

// Формат даты

fun formatDate(dateString: String): String {
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

// Окно статистики

@Composable
fun DialogCalendarHabitInfo(
    id: Int,
    image: Int?,
    onDismissRequest: () -> Unit,
) {
    val vm: CalendarViewModel = koinViewModel()

    LaunchedEffect(id) {
        vm.initialization(id)
    }

    val infoDetail by vm.dayListInfoDetail.collectAsState()

    val sortedInfo = infoDetail.sortedBy {
        LocalDate.parse(
            it.calendarDate,
            DateTimeFormatter.ofPattern("yyyy-M-d")
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(
                1.dp,
                HabitInfoBorder
            ),
            colors = CardDefaults.cardColors(
                containerColor = HabitInfoBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 16.dp
                )
            ) {

                // Шапка

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                id = image ?: R.drawable.card_add_icon
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.width(13.dp))

                    Column {
                        Text(
                            text = "Статистика привычки",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = HabitInfoText
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "История выполнения",
                            fontSize = 12.sp,
                            color = HabitInfoSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = HabitInfoDivider,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Список статистики

                if (sortedInfo.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Пока нет записей",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HabitInfoText
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Здесь появится история привычки",
                            fontSize = 12.sp,
                            color = HabitInfoSecondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 460.dp),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 4.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(sortedInfo) { habit ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(
                                    1.dp,
                                    HabitInfoBorder
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 0.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(15.dp)
                                ) {

                                    // Дата

                                    Text(
                                        text = formatDate(habit.calendarDate),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HabitInfoBlueDark
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Цена

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Цена",
                                            fontSize = 13.sp,
                                            color = HabitInfoSecondary
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = if (
                                                habit.calendarHabitPrice.toInt()
                                                    .toFloat() == habit.calendarHabitPrice
                                            ) {
                                                habit.calendarHabitPrice.toInt().toString()
                                            } else {
                                                habit.calendarHabitPrice.toString()
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = HabitInfoText
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Количество

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Количество",
                                            fontSize = 13.sp,
                                            color = HabitInfoSecondary
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = if (
                                                habit.calendarHabitQuantity.toInt()
                                                    .toFloat() == habit.calendarHabitQuantity
                                            ) {
                                                habit.calendarHabitQuantity.toInt().toString()
                                            } else {
                                                habit.calendarHabitQuantity.toString()
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = HabitInfoText
                                        )
                                    }

                                    // Комментарий

                                    if (habit.calendarHabitDescription.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(12.dp))

                                        HorizontalDivider(
                                            color = HabitInfoDivider,
                                            thickness = 1.dp
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "Комментарий",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = HabitInfoSecondary
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = habit.calendarHabitDescription,
                                            fontSize = 13.sp,
                                            color = HabitInfoText,
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}