package com.example.habbittracker.presentation.dialog.dialogHabitlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.habbittracker.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.habbittracker.ui.theme.ButtonCancelColor
import com.example.habbittracker.ui.theme.ButtonConfirmColor
import com.example.habbittracker.ui.theme.CardDayColorGray
import com.example.habbittracker.ui.theme.HabitDayColor
import com.example.habbittracker.ui.theme.TextCardNumberColor

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
                .height(625.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                Text(
                    text = "Выберите иконку",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(selectedIcon),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Текущий выбор",
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    modifier = Modifier.weight(1f),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(iconList.size) { item ->
                        val iconRes = iconList[item]
                        Card(
                            onClick = {
                                selectedIcon = iconRes
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color =
                                    if (selectedIcon == iconRes)
                                        HabitDayColor
                                    else
                                        Color.LightGray
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (selectedIcon == iconRes)
                                        CardDayColorGray
                                    else
                                        MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp),
                                contentAlignment = Alignment.Center,

                            ) {
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextCardNumberColor
                        ),
                        border = BorderStroke(
                            2.dp,
                            ButtonCancelColor
                        )
                    )
                    {
                        Text("Отмена")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonConfirmColor
                        ),
                        onClick = {
                            onSave(selectedIcon)
                            onDismiss()
                        },
                    )
                    {
                        Text("Сохранить", maxLines = 1)
                    }
                }
            }
        }
    }
}