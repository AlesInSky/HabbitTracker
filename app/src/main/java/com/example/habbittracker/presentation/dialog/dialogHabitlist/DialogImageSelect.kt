package com.example.habbittracker.presentation.dialog.dialogHabitlist

import com.example.habbittracker.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogImageSelect(
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    val iconList = listOf(
        R.drawable.alcohol_icon,
        R.drawable.alcohol2_icon,
        R.drawable.smoke_icon,
        R.drawable.smoke2_icon,
        R.drawable.emotion_icon,
        R.drawable.emotion2_icon,
        R.drawable.emotion3_icon,
        R.drawable.emotion4_icon,
        R.drawable.emotion5_icon,
        R.drawable.food_icon,
        R.drawable.food2_icon,
        R.drawable.game_icon,
        R.drawable.game2_icon,
        R.drawable.look_icon,
        R.drawable.look2_icon,
        R.drawable.pharm_icon,
        R.drawable.pharm2_icon,
        R.drawable.shop_icon,
        R.drawable.sleep_icon,
    )

    var selectedIcon by remember { mutableStateOf(iconList.first()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Выберите иконку")

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(iconList.size) { item ->
                        val iconRes = iconList[item]
                        IconButton(
                            onClick = { selectedIcon = iconRes }
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            onSave(selectedIcon)
                            onDismiss()
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}