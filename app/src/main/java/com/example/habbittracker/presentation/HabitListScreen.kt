package com.example.habbittracker.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.R
import com.example.habbittracker.presentation.dialog.dialogHabitlist.DialogEditHabit
import com.example.habbittracker.presentation.dialog.dialogHabitlist.DialogNewHabit
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import org.koin.androidx.compose.koinViewModel

private val HabitListBlue = Color(0xFF5798D5)
private val HabitListBlueDark = Color(0xFF367DBD)
private val HabitListBlueLight = Color(0xFFEAF4FC)
private val HabitListText = Color(0xFF263747)
private val HabitListSecondary = Color(0xFF94A3B1)
private val HabitListBorder = Color(0xFFEAF0F5)
private val HabitListBackground = Color.White
private val HabitListSwipeBackground = Color(0xFFF4F8FC)

@Composable
fun HabitListScreen(
    navController: NavController,
) {
    val vm: HabitViewModel = koinViewModel()
    val habitList by vm.habitList.collectAsState()
    val editHabit by vm.editHabit.collectAsState()

    HabitList(
        habitList = habitList,
        editHabit = editHabit,
        vm = vm
    )
}

@Composable
fun HabitList(
    habitList: List<HabitEntity>,
    editHabit: HabitEntity?,
    vm: HabitViewModel,
) {
    val visibleHabits = habitList.filter { !it.isDeleted }

    if (visibleHabits.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Привычек пока нет",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = HabitListText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Нажмите +, чтобы создать первую привычку",
                style = MaterialTheme.typography.bodyMedium,
                color = HabitListSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            AddHabitCard(vm = vm)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Заголовок
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Список привычек",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = HabitListText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Все ваши привычки в одном месте",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HabitListSecondary
                    )
                }
            }

            // Список привычек
            items(visibleHabits) { habit ->
                HabitCard(
                    habit = habit,
                    vm = vm
                )
            }

            // Кнопка добавления
            item {
                AddHabitCard(vm = vm)
            }
        }
    }

    if (editHabit != null) {
        DialogEditHabit(
            habit = editHabit,
            onDismissRequest = {
                vm.closeEditDialog()
            },
            onSave = {
                vm.updateHabit(it)
            }
        )
    }
}

@Composable
fun HabitCard(
    habit: HabitEntity,
    vm: HabitViewModel,
) {
    val archive = SwipeAction(
        icon = {
            Icon(
                painter = painterResource(
                    R.drawable.ic_delete_black
                ),
                contentDescription = "Удалить",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        },
        background = Color(0xFFE36B6B),
        onSwipe = {
            vm.deleteHabit(habit)
        }
    )

    SwipeableActionsBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        endActions = listOf(archive),
        swipeThreshold = 100.dp,
        backgroundUntilSwipeThreshold = HabitListSwipeBackground
    ) {
        Card(
            onClick = {
                vm.openEditDialog(habit)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = HabitListBackground
            ),
            border = BorderStroke(
                1.dp,
                HabitListBorder
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка привычки
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = HabitListBlueLight,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = habit.habitImage
                                ?: R.drawable.card_add_icon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Название привычки
                Text(
                    text = habit.habitName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = HabitListText,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun AddHabitCard(
    vm: HabitViewModel,
) {
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = {
            showConfirmDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = HabitListBlueLight
        ),
        border = BorderStroke(
            1.dp,
            HabitListBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.card_add_icon
                ),
                contentDescription = "Добавить привычку",
                modifier = Modifier.size(26.dp),
                tint = HabitListBlue
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Добавить привычку",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = HabitListBlueDark
            )
        }
    }

    if (showConfirmDialog) {
        DialogNewHabit(
            habit = HabitEntity(),
            onDismissRequest = {
                showConfirmDialog = false
            },
            onSave = { newHabit ->
                vm.addHabit(newHabit)
                showConfirmDialog = false
            },
            vm = vm
        )
    }
}