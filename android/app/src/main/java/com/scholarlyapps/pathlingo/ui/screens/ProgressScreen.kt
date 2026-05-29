package com.scholarlyapps.pathlingo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager

@Composable
fun ProgressScreen() {
    val dm = DataManager.getInstance()
    val user = remember { dm.getUser() }
    val categories = remember { dm.getCategories() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.paper)),
        contentPadding = PaddingValues(bottom = 100.dp),
    ) {
        item {
            Text(
                text = "Progress",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.ink),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            )
        }

        if (user != null) {
            item {
                val xpPercent = if (user.maxXp > 0) user.xp / user.maxXp.toFloat() else 0f
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.sage_deep)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Level ${user.level}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                                Text(
                                    text = "${user.xp} / ${user.maxXp} XP",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "🔥 ${user.streak}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.butter),
                                )
                                Text(
                                    text = "day streak",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0x33000000)),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(xpPercent)
                                    .fillMaxSize()
                                    .background(Color.White),
                            )
                        }
                    }
                }
            }
        }

        items(categories) { category ->
            val progress = category.progress / 100f
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    ) {
                        Text(
                            text = category.en,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.ink),
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "${category.progress}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.sage_deep),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0D8CC)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxSize()
                                .background(colorResource(R.color.sage_deep)),
                        )
                    }
                    Text(
                        text = "${category.count} words",
                        fontSize = 11.sp,
                        color = colorResource(R.color.ink_soft),
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
    }
}
