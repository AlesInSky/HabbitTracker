package com.example.habbittracker.presentation.dialog.dialogHabitlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.habbittracker.R

private val SelectIconBlue = Color(0xFF5798D5)
private val SelectIconBlueLight = Color(0xFFEAF4FC)
private val SelectIconText = Color(0xFF263747)
private val SelectIconSecondary = Color(0xFF94A3B1)
private val SelectIconDivider = Color(0xFFEDF1F5)
private val SelectIconBorder = Color(0xFFEAF0F5)
private val SelectIconBackground = Color.White

@Composable
fun DialogImageSelect(
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit,
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

    var selectedIcon by remember {
        mutableStateOf(iconList.first())
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(625.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = SelectIconBackground
            ),
            border = BorderStroke(1.dp, SelectIconBorder),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Выберите иконку",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SelectIconText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Выберите изображение для привычки",
                    fontSize = 13.sp,
                    color = SelectIconSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(
                                color = SelectIconBlueLight,
                                shape = RoundedCornerShape(22.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(selectedIcon),
                            contentDescription = null,
                            modifier = Modifier.size(58.dp),
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Текущий выбор",
                        fontSize = 13.sp,
                        color = SelectIconSecondary
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = SelectIconDivider
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(iconList) { iconRes ->
                        val isSelected = selectedIcon == iconRes

                        Card(
                            onClick = {
                                selectedIcon = iconRes
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(82.dp),
                            shape = RoundedCornerShape(17.dp),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) {
                                    SelectIconBlue
                                } else {
                                    SelectIconBorder
                                }
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    SelectIconBlueLight
                                } else {
                                    SelectIconBackground
                                }
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(42.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SelectIconText
                        ),
                        border = BorderStroke(
                            1.dp,
                            SelectIconBorder
                        )
                    ) {
                        Text(
                            text = "Отмена",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        onClick = {
                            onSave(selectedIcon)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SelectIconBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
                        )
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