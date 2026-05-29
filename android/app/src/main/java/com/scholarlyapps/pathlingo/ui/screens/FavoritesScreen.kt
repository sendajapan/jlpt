package com.scholarlyapps.pathlingo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.models.Word

@Composable
fun FavoritesScreen() {
    val favorites = remember {
        DataManager.getInstance().getCategories().flatMap { cat ->
            cat.subcategories.flatMap { sub ->
                sub.words.filter { it.favorite }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.paper)),
    ) {
        Text(
            text = "Favorites",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.ink),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 100.dp
            ),
        ) {
            items(favorites) { word ->
                WordCard(word)
            }
        }
    }
}

@Composable
fun WordCard(word: Word) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = word.kanji,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink),
                )
                Text(
                    text = "${word.reading} · ${word.romaji}",
                    fontSize = 12.sp,
                    color = colorResource(R.color.ink_soft),
                )
            }
            Text(
                text = word.en,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.terra_deep),
            )
        }
    }
}
