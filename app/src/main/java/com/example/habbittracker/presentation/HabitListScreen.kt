package com.example.habbittracker.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habbittracker.R
import com.example.data.local.entity.HabitEntity
import com.example.habbittracker.presentation.dialog.dialogHabitlist.DialogEditHabit
import com.example.habbittracker.presentation.dialog.dialogHabitlist.DialogNewHabit
import com.example.habbittracker.presentation.viewmodel.HabitViewModel
import com.example.habbittracker.ui.theme.CardDayColorGray
import org.koin.androidx.compose.koinViewModel

@Composable
fun HabitListScreen(
    navController: NavController,
) {

    @Composable
    fun HabitCard(habit: HabitEntity) {
        val vm: HabitViewModel = koinViewModel()
        val editHabit by vm.editHabit.collectAsState()

        Card(
            onClick = { vm.openEditDialog(habit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardDayColorGray
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка
                Icon(
                    painter = painterResource(id = habit.habitImage ?: R.drawable.card_add_icon),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Habit icon"
                )


                Text(text = habit.habitName, modifier = Modifier.weight(1f))

                // Кнопка редактирования. Пока не решил, как лучше открывать редактирование
//                IconButton(onClick = { vm.openEditDialog(habit) }) {
//                    Icon(
//                        painter = painterResource(R.drawable.edit_icon),
//                        contentDescription = "Edit"
//                    )
//                }

                // Кнопка удаления
                IconButton(onClick = {
                    vm.deleteHabit(habit)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "Delete"
                    )
                }
            }
        }
        if (editHabit != null) {
            DialogEditHabit(
                habit = editHabit,
                onDismissRequest = { vm.closeEditDialog() },
                onSave = { vm.updateHabit(it) }
            )
        }
    }

    @Composable
    fun AddHabitCard() {
        val vm: HabitViewModel = koinViewModel()
        var showConfirmDialog by remember { mutableStateOf(false) }

        Card(
            onClick = {
                showConfirmDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardDayColorGray
            )
        )
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.card_add_icon),
                    contentDescription = "Add"
                )
            }
        }
        if (showConfirmDialog) {
            DialogNewHabit(
                habit = HabitEntity(),
                onDismissRequest = { showConfirmDialog = false },
                onSave = { newHabit ->
                    vm.addHabit(newHabit)
                    showConfirmDialog = false
                },
                vm = vm
            )
        }
    }

    @Composable
    fun HabitList() {
        val vm: HabitViewModel = koinViewModel()
        val habitList by vm.habitList.collectAsState()
        val visibleHabits = habitList.filter { !it.isDeleted }

        if (visibleHabits.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Привычек пока нет",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Нажмите + чтобы создать первую привычку",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                AddHabitCard()
            }

        } else {

            LazyColumn(
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(visibleHabits) { habit ->
                    HabitCard(habit)
                }

                item {
                    AddHabitCard()
                }
            }
        }
    }

    HabitList()
}