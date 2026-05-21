package com.scholarlyapps.pathlingo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.ui.auth.LogoutHelper

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val user = remember { DataManager.getInstance().getUser() }

    var soundEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.paper)),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = 24.dp, bottom = 100.dp
        ),
    ) {
        item {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.ink),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp).padding(bottom = 16.dp),
            )
        }

        if (user != null) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.sage_deep)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(colorResource(R.color.sage))
                                .padding(end = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_user),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp),
                        ) {
                            Text(
                                text = user.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            Text(
                                text = user.jpName,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f),
                            )
                        }
                        Text(
                            text = "Lv. ${user.level}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.sage_deep),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatCard(
                        value = "${user.wordsKnown}",
                        label = "words learned",
                        valueColor = colorResource(R.color.ink),
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        value = "${user.streak}",
                        label = "day streak",
                        valueColor = colorResource(R.color.terra),
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        value = "${user.accuracy}%",
                        label = "accuracy",
                        valueColor = colorResource(R.color.sage_deep),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        item {
            Text(
                text = "PREFERENCES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.ink_soft),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
            )
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    SettingsRow(
                        iconRes = R.drawable.ic_speaker,
                        label = "Sound effects",
                        checked = soundEnabled,
                        onCheckedChange = { soundEnabled = it },
                    )
                    Divider()
                    SettingsRow(
                        iconRes = R.drawable.ic_bell,
                        label = "Daily reminders",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                    )
                    Divider()
                    SettingsRow(
                        iconRes = R.drawable.ic_moon,
                        label = "Dark mode",
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it },
                    )
                }
            }
        }

        if (user != null) {
            item {
                Text(
                    text = "ACCOUNT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink_soft),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                )
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_globe),
                                contentDescription = null,
                                tint = colorResource(R.color.ink_soft),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 14.dp),
                            )
                            Text(
                                text = user.email,
                                fontSize = 15.sp,
                                color = colorResource(R.color.ink),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 14.dp),
                            )
                        }
                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { LogoutHelper.logout(context) }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_logout),
                                contentDescription = null,
                                tint = colorResource(R.color.terra_deep),
                                modifier = Modifier.size(20.dp),
                            )
                            Text(
                                text = "Log out",
                                fontSize = 15.sp,
                                color = colorResource(R.color.terra_deep),
                                modifier = Modifier.padding(start = 14.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        modifier = modifier.padding(bottom = 12.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Text(text = label, fontSize = 11.sp, color = colorResource(R.color.ink_soft))
        }
    }
}

@Composable
private fun SettingsRow(
    iconRes: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colorResource(R.color.ink_soft),
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = label,
            fontSize = 15.sp,
            color = colorResource(R.color.ink),
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 16.dp)
            .background(colorResource(R.color.paper)),
    )
}
