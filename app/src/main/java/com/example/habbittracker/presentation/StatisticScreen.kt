package com.example.habbittracker.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habbittracker.presentation.viewmodel.*
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

private val Blue = Color(0xFF5798D5)
private val BlueDark = Color(0xFF367DBD)
private val BlueLight = Color(0xFFEAF4FC)
private val TextMain = Color(0xFF263747)
private val Secondary = Color(0xFF94A3B1)
private val DividerColor = Color(0xFFEDF1F5)

private val ChartColors = listOf(
    Color(0xFF5798D5),
    Color(0xFF75B8E8),
    Color(0xFF8BC7B8),
    Color(0xFFFFC477),
    Color(0xFFB19CD9),
    Color(0xFFE99AA5),
    Color(0xFF6FA5A0)
)

@Composable
fun StatisticScreen(navController: NavController) {
    val vm: StatisticViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Статистика",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        item {
            PeriodSelector(
                selectedPeriod = state.period,
                onPeriodSelected = vm::selectPeriod
            )
        }

        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Blue)
                }
            }
        } else {
            item {
                Text(
                    text = "Общая сводка",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
            }

            item {
                SummaryCard(
                    totalExpenses = state.totalExpenses,
                    recordsCount = state.recordsCount,
                    activeDaysCount = state.activeDaysCount
                )
            }

            item {
                Text(
                    text = "Динамика расходов",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
            }

            item {
                ExpenseChart(
                    points = state.expensePoints,
                    habits = state.habits,
                    selectedHabitIds = state.selectedHabitIds,
                    chartType = state.chartType,
                    onSwipeLeft = vm::nextChartType,
                    onSwipeRight = vm::previousChartType
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Привычки",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Удалённые",
                            fontSize = 12.sp,
                            color = Secondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = state.showDeletedHabits,
                            onCheckedChange = vm::setShowDeletedHabits
                        )
                    }
                }
            }

            if (state.habits.isEmpty()) {
                item {
                    Text(
                        text = "За выбранный период записей пока нет",
                        color = Secondary,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            } else {
                items(
                    items = state.habits,
                    key = { it.habitId }
                ) { habit ->
                    HabitStatisticCard(
                        habit = habit,
                        selected = state.selectedHabitIds == null ||
                                habit.habitId in state.selectedHabitIds!!,
                        onClick = {
                            vm.toggleHabitSelection(habit.habitId)
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: StatisticPeriod,
    onPeriodSelected: (StatisticPeriod) -> Unit,
) {
    val periods = listOf(
        StatisticPeriod.MONTH to "Месяц",
        StatisticPeriod.WEEK to "Неделя",
        StatisticPeriod.YEAR to "Год",
        StatisticPeriod.ALL to "Всё время",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        periods.forEach { (period, title) ->
            val selected = selectedPeriod == period

            Button(
                onClick = { onPeriodSelected(period) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) Blue else BlueLight,
                    contentColor = if (selected) Color.White else BlueDark
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    totalExpenses: Float,
    recordsCount: Int,
    activeDaysCount: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BlueLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Всего расходов",
                color = Secondary,
                fontSize = 13.sp
            )

            Text(
                text = formatAmount(totalExpenses),
                color = BlueDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryValue("Записи", recordsCount.toString())
                SummaryValue("Активные дни", activeDaysCount.toString())
            }
        }
    }
}

@Composable
private fun SummaryValue(title: String, value: String) {
    Column {
        Text(
            text = value,
            color = TextMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = title, color = Secondary, fontSize = 12.sp)
    }
}

@Composable
private fun ExpenseChart(
    points: List<StatisticExpensePoint>,
    habits: List<HabitStatisticItem>,
    selectedHabitIds: Set<Long>?,
    chartType: StatisticChartType,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
) {
    val selectedHabits = habits.filter {
        selectedHabitIds == null || it.habitId in selectedHabitIds
    }

    val title = when (chartType) {
        StatisticChartType.BAR -> "Столбчатый график"
        StatisticChartType.LINE -> "Линейный график"
        StatisticChartType.PIE -> "Распределение расходов"
    }

    var dragAmount by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(chartType) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, amount ->
                        dragAmount += amount
                    },
                    onDragEnd = {
                        when {
                            dragAmount < -50f -> onSwipeLeft()
                            dragAmount > 50f -> onSwipeRight()
                        }
                        dragAmount = 0f
                    },
                    onDragCancel = {
                        dragAmount = 0f
                    }
                )
            }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = TextMain,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = when (chartType) {
                            StatisticChartType.BAR -> "1 / 3"
                            StatisticChartType.LINE -> "2 / 3"
                            StatisticChartType.PIE -> "3 / 3"
                        },
                        color = Secondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Свайпните, чтобы сменить график",
                    color = Secondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (chartType) {
                    StatisticChartType.BAR -> BarChart(points, selectedHabits)
                    StatisticChartType.LINE -> LineChart(points, selectedHabits)
                    StatisticChartType.PIE -> PieChart(points, selectedHabits)
                }

                if (selectedHabits.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    selectedHabits.forEachIndexed { index, habit ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(ChartColors[index % ChartColors.size])
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = habit.name,
                                color = TextMain,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = formatAmount(habit.totalExpense),
                                color = Secondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BarChart(
    points: List<StatisticExpensePoint>,
    habits: List<HabitStatisticItem>,
) {
    if (points.isEmpty() || habits.isEmpty()) {
        Text("Нет данных для графика", color = Secondary)
        return
    }

    val maxValue = points.maxOfOrNull { point ->
        habits.sumOf {
            (point.values[it.habitId] ?: 0f).toDouble()
        }
    }?.toFloat() ?: 0f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val chartHeight = size.height
        val groupWidth = size.width / points.size
        val barWidth = (groupWidth * 0.65f / habits.size).coerceAtLeast(2f)

        drawLine(
            color = DividerColor,
            start = Offset(0f, chartHeight - 1f),
            end = Offset(size.width, chartHeight - 1f),
            strokeWidth = 1.dp.toPx()
        )

        points.forEachIndexed { pointIndex, point ->
            habits.forEachIndexed { habitIndex, habit ->
                val value = point.values[habit.habitId] ?: 0f
                val barHeight = if (maxValue > 0f) {
                    value / maxValue * (chartHeight - 8.dp.toPx())
                } else 0f

                val totalBarsWidth = barWidth * habits.size
                val startX = pointIndex * groupWidth +
                        (groupWidth - totalBarsWidth) / 2f
                val x = startX + habitIndex * barWidth

                drawRoundRect(
                    color = ChartColors[habitIndex % ChartColors.size],
                    topLeft = Offset(x, chartHeight - barHeight),
                    size = Size(barWidth * 0.8f, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        5.dp.toPx()
                    )
                )
            }
        }
    }

    ChartLabels(points)
}

@Composable
private fun LineChart(
    points: List<StatisticExpensePoint>,
    habits: List<HabitStatisticItem>,
) {
    if (points.isEmpty() || habits.isEmpty()) {
        Text("Нет данных для графика", color = Secondary)
        return
    }

    val maxValue = points.maxOfOrNull { point ->
        habits.maxOfOrNull {
            point.values[it.habitId] ?: 0f
        } ?: 0f
    } ?: 0f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val left = 8.dp.toPx()
        val right = size.width - 8.dp.toPx()
        val top = 8.dp.toPx()
        val bottom = size.height - 8.dp.toPx()

        drawLine(
            color = DividerColor,
            start = Offset(left, bottom),
            end = Offset(right, bottom),
            strokeWidth = 1.dp.toPx()
        )

        habits.forEachIndexed { habitIndex, habit ->
            val color = ChartColors[habitIndex % ChartColors.size]

            val coordinates = points.mapIndexed { index, point ->
                val x = if (points.size <= 1) {
                    (left + right) / 2f
                } else {
                    left + (right - left) * index / (points.size - 1)
                }

                val value = point.values[habit.habitId] ?: 0f
                val y = if (maxValue > 0f) {
                    bottom - value / maxValue * (bottom - top)
                } else bottom

                Offset(x, y)
            }

            coordinates.zipWithNext().forEach { (start, end) ->
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            coordinates.forEach { point ->
                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }
    }

    ChartLabels(points)
}

@Composable
private fun PieChart(
    points: List<StatisticExpensePoint>,
    habits: List<HabitStatisticItem>,
) {
    if (points.isEmpty() || habits.isEmpty()) {
        Text("Нет данных для диаграммы", color = Secondary)
        return
    }

    val totals = habits.map { habit ->
        habit to points.sumOf {
            (it.values[habit.habitId] ?: 0f).toDouble()
        }.toFloat()
    }.filter { it.second > 0f }

    val total = totals.sumOf { it.second.toDouble() }.toFloat()

    if (total <= 0f) {
        Text("Нет расходов для диаграммы", color = Secondary)
        return
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        val diameter = minOf(size.width, size.height) * 0.85f
        val left = (size.width - diameter) / 2f
        val top = (size.height - diameter) / 2f

        var startAngle = -90f

        totals.forEachIndexed { index, item ->
            val sweep = item.second / total * 360f

            drawArc(
                color = ChartColors[index % ChartColors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                topLeft = Offset(left, top),
                size = Size(diameter, diameter),
                style = Fill
            )

            startAngle += sweep
        }
    }
}

@Composable
private fun ChartLabels(points: List<StatisticExpensePoint>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        points.forEach { point ->
            Text(
                text = point.label,
                fontSize = 10.sp,
                color = Secondary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

private fun formatAmount(amount: Float): String {
    return String.format(Locale("ru", "RU"), "%.2f", amount)
}

@Composable
private fun HabitStatisticCard(
    habit: HabitStatisticItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) BlueLight else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 1.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = habit.name,
                    color = TextMain,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatAmount(habit.totalExpense),
                    color = Secondary,
                    fontSize = 13.sp
                )
            }

            if (selected) {
                Text(
                    text = "✓",
                    color = BlueDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}