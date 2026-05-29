package com.scholarlyapps.pathlingo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.models.Subcategory

@Composable
fun SubcategoryScreen(categoryId: String?, navController: NavController) {
    val category = remember(categoryId) { DataManager.getInstance().getCategoryById(categoryId) }

    if (category == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(R.color.paper))) {
        Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            AsyncImage(
                model = category.bg,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xBB000000)),
                        )
                    ),
            )
            AsyncImage(
                model = category.iconUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 20.dp, end = 160.dp),
            ) {
                Text(
                    text = "${category.jp} · ${category.en}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = "${category.count} words  ·  ${category.subcategories.size} chapters",
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            items(category.subcategories) { subcat ->
                SubcategoryCard(
                    subcat = subcat,
                    onClick = {
                        navController.navigate("word_detail/${category.id}/${subcat.id}/0")
                    },
                )
            }
        }
    }
}

@Composable
fun SubcategoryCard(subcat: Subcategory, onClick: () -> Unit) {
    val accentColor = when (subcat.bg) {
        "butter" -> colorResource(R.color.butter)
        "lav" -> colorResource(R.color.lav)
        "sky" -> colorResource(R.color.sky)
        "rose" -> colorResource(R.color.rose)
        "terra" -> colorResource(R.color.terra)
        else -> colorResource(R.color.sage_deep)
    }
    val progress = if (subcat.total > 0) subcat.mastered / subcat.total.toFloat() else 0f

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        onClick = onClick,
        enabled = !subcat.locked && subcat.words.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = subcat.jp,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.ink),
                        modifier = Modifier.weight(1f),
                    )
                    if (subcat.locked) {
                        Text(text = "🔒", fontSize = 14.sp)
                    }
                }
                Text(
                    text = subcat.en,
                    fontSize = 12.sp,
                    color = colorResource(R.color.ink_soft),
                    modifier = Modifier.padding(top = 1.dp, bottom = 8.dp),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE0D8CC)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxSize()
                            .background(accentColor),
                    )
                }
                Text(
                    text = "${subcat.mastered} / ${subcat.total} words",
                    fontSize = 11.sp,
                    color = colorResource(R.color.ink_mute),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (subcat.iconUrl.isNotEmpty()) {
                AsyncImage(
                    model = subcat.iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(start = 12.dp),
                )
            }
        }
    }
}
