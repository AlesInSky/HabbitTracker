package com.example.habbittracker.presentation.dialog.dialogCalendar

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.habbittracker.presentation.viewmodel.CalendarViewModel
import com.example.habbittracker.R
import org.koin.androidx.compose.koinViewModel

import com.example.habbittracker.ui.theme.CardDayColorGray
import com.example.habbittracker.ui.theme.TextCardNumberColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    val infoDetail = vm.dayListInfoDetail
        .collectAsState()
        .value
        .sortedBy {
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
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            // Шапка
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(
                        id = image ?: R.drawable.card_add_icon
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Статистика привычки",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextCardNumberColor
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CardDayColorGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(infoDetail) { habit ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        colors = CardDefaults.cardColors(
                            containerColor = CardDayColorGray
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = formatDate(habit.calendarDate),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row {
                                Text(
                                    text = "Цена: ",
                                    color = TextCardNumberColor,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = if (habit.calendarHabitPrice.toInt()
                                            .toFloat() == habit.calendarHabitPrice
                                    )
                                        habit.calendarHabitPrice.toInt().toString()
                                    else habit.calendarHabitPrice.toString(),
                                    color = Color.DarkGray,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                Text(
                                    text = "Количество: ",
                                    color = TextCardNumberColor,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                    text = if (habit.calendarHabitQuantity.toInt()
                                            .toFloat() == habit.calendarHabitQuantity
                                    )
                                        habit.calendarHabitQuantity.toInt().toString()
                                    else habit.calendarHabitQuantity.toString(),
                                    color = Color.DarkGray,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Row {
                                Text(
                                    text = if (habit.calendarHabitDescription != "")
                                        "Комментарий: ${habit.calendarHabitDescription}" else "",
                                    color = TextCardNumberColor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    minLines = 1,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}