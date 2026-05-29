package com.scholarlyapps.pathlingo.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager

@Composable
fun ScoreScreen(navController: NavController) {
    val lessonData = remember { DataManager.getInstance().getLessonData() }
    val score = lessonData?.optInt("score", 86) ?: 86
    val maxScore = lessonData?.optInt("maxScore", 100) ?: 100
    val stars = lessonData?.optInt("stars", 3) ?: 3
    val xpEarned = lessonData?.optInt("xpEarned", 85) ?: 85
    val correctCount = lessonData?.optInt("correctCount", 4) ?: 4
    val totalCount = lessonData?.optInt("totalCount", 5) ?: 5
    val time = lessonData?.optString("time", "2:14") ?: "2:14"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.paper))
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))

            AsyncImage(
                model = R.drawable.ic_owl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
            )

            Text(
                text = "すばらしい！ · Wonderful!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.ink),
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "YOUR SCORE",
                        fontSize = 10.sp,
                        color = colorResource(R.color.ink_soft),
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "$score/$maxScore",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.ink),
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0D8CC)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(score / maxScore.toFloat())
                                .fillMaxSize()
                                .background(colorResource(R.color.sage_deep)),
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(3) { i ->
                            Text(text = if (i < stars) "⭐" else "☆", fontSize = 24.sp)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        ScoreStat(value = "+$xpEarned XP", color = colorResource(R.color.terra))
                        ScoreStat(value = "$correctCount/$totalCount", color = colorResource(R.color.sage))
                        ScoreStat(value = time, color = colorResource(R.color.terra))
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.sage)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LEVEL 7  APPRENTICE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x33000000)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.64f)
                                .fillMaxSize()
                                .background(Color.White),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        Text(
                            text = "320/500 XP",
                            fontSize = 11.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "180 to go",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.butter),
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = false }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.terra)),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(
                text = "Continue",
                color = colorResource(R.color.white),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun ScoreStat(value: String, color: Color) {
    Text(
        text = value,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = color,
    )
}
