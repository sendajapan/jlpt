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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.models.Word

@Composable
fun WordDetailScreen(
    categoryId: String?,
    subcategoryId: String?,
    initialIndex: Int,
    navController: NavController,
) {
    val dm = DataManager.getInstance()
    val category = remember(categoryId) { dm.getCategoryById(categoryId) }
    val subcat = remember(categoryId, subcategoryId) {
        category?.subcategories?.find { it.id == subcategoryId }
    }

    if (subcat == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    val words = subcat.words
    var currentIndex by rememberSaveable { mutableIntStateOf(initialIndex) }
    val currentWord: Word? = words.getOrNull(currentIndex)

    Column(modifier = Modifier.fillMaxSize().background(colorResource(R.color.paper))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(colorResource(R.color.sage)),
        ) {
            AsyncImage(
                model = subcat.iconUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        if (currentWord != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "JAPANESE",
                            fontSize = 10.sp,
                            color = colorResource(R.color.ink_soft),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        Text(
                            text = currentWord.kanji,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.ink),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        Text(
                            text = currentWord.reading,
                            fontSize = 14.sp,
                            color = colorResource(R.color.ink_soft),
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colorResource(R.color.paper))
                                .padding(bottom = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ROMAJI",
                            fontSize = 10.sp,
                            color = colorResource(R.color.ink_soft),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        Text(
                            text = currentWord.romaji,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = colorResource(R.color.ink),
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                        Text(
                            text = "ENGLISH",
                            fontSize = 10.sp,
                            color = colorResource(R.color.ink_soft),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        Text(
                            text = currentWord.en,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.terra_deep),
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.sage)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "EXAMPLE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = currentWord.example_jp,
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Text(
                            text = currentWord.example_romaji,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Text(
                            text = currentWord.example_en,
                            fontSize = 12.sp,
                            color = Color.White,
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp),
                ) {
                    listOf(currentWord.type, currentWord.jlpt, "+${currentWord.xp} XP").forEach { chip ->
                        Text(
                            text = chip,
                            color = colorResource(R.color.white),
                            fontSize = 11.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(colorResource(R.color.sage))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentIndex < words.size - 1) {
                            currentIndex++
                        } else {
                            navController.navigate("score") {
                                popUpTo("score") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.terra)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(
                        text = if (currentIndex < words.size - 1) "Next" else "Finish",
                        color = colorResource(R.color.white),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
