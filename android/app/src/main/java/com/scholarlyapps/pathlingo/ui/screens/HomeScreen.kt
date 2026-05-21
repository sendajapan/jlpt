package com.scholarlyapps.pathlingo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.models.Category
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    val dm = DataManager.getInstance()
    val user = remember { dm.getUser() }
    val categories = remember { dm.getCategories() }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.paper)),
    ) {
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.sage)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null,
                            tint = colorResource(R.color.white),
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                    ) {
                        Text(
                            text = SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(Date()),
                            fontSize = 12.sp,
                            color = colorResource(R.color.ink_soft),
                        )
                        Text(
                            text = user?.name ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.ink),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(R.color.white)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = null,
                            tint = colorResource(R.color.ink),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                Text(
                    text = "こんにちは！",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink),
                    modifier = Modifier.padding(top = 32.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Categories",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.ink),
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        color = colorResource(R.color.sage_deep),
                        modifier = Modifier.clickable { navController.navigate("categories") },
                    )
                }
            }
        }

        items(categories) { category ->
            CategoryCard(
                category = category,
                onClick = { navController.navigate("subcategory/${category.id}") },
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    val context = LocalContext.current
    val bgModel: Any? = remember(category.bg) {
        when {
            category.bg.isEmpty() -> null
            category.bg.startsWith("http") -> category.bg
            else -> {
                val res = context.resources.getIdentifier(
                    "bg_cat_${category.id}_rounded", "drawable", context.packageName
                )
                if (res != 0) res else null
            }
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.sage)),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (bgModel != null) {
                AsyncImage(
                    model = bgModel,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            if (category.iconUrl.isNotEmpty()) {
                AsyncImage(
                    model = category.iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 14.dp, top = 14.dp, end = 128.dp),
            ) {
                Text(
                    text = category.jp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink),
                )
                Text(
                    text = category.ch,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink),
                )
            }

            if (category.locked) {
                Text(
                    text = "🔒",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp),
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 14.dp, bottom = 14.dp, end = 128.dp),
            ) {
                Text(
                    text = category.en,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.ink_soft),
                )
                Text(
                    text = "${category.count} words",
                    fontSize = 11.sp,
                    color = colorResource(R.color.ink_soft),
                )
            }
        }
    }
}
