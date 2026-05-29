package com.scholarlyapps.pathlingo.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scholarlyapps.pathlingo.R
import com.scholarlyapps.pathlingo.ui.screens.CategoryListScreen
import com.scholarlyapps.pathlingo.ui.screens.FavoritesScreen
import com.scholarlyapps.pathlingo.ui.screens.HomeScreen
import com.scholarlyapps.pathlingo.ui.screens.ProgressScreen
import com.scholarlyapps.pathlingo.ui.screens.ScoreScreen
import com.scholarlyapps.pathlingo.ui.screens.SettingsScreen
import com.scholarlyapps.pathlingo.ui.screens.SubcategoryScreen
import com.scholarlyapps.pathlingo.ui.screens.WordDetailScreen

private data class NavTab(val iconRes: Int, val label: String, val route: String)

class MainDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val tabs = listOf(
        NavTab(R.drawable.ic_home, "Home", "home"),
        NavTab(R.drawable.ic_heart, "Saved", "favorites"),
        NavTab(R.drawable.ic_chart, "Progress", "progress"),
        NavTab(R.drawable.ic_settings, "Settings", "settings"),
    )
    val tabRoutes = tabs.map { it.route }.toSet()

    Scaffold(
        containerColor = Color(0xFFF6EBD7),
        bottomBar = {
            if (currentRoute in tabRoutes) {
                FloatingBottomNav(
                    tabs = tabs,
                    selectedRoute = currentRoute,
                    onTabSelected = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding),
        ) {
            composable("home") { HomeScreen(navController) }
            composable("categories") { CategoryListScreen(navController) }
            composable("subcategory/{categoryId}") { entry ->
                SubcategoryScreen(
                    categoryId = entry.arguments?.getString("categoryId"),
                    navController = navController,
                )
            }
            composable("word_detail/{categoryId}/{subcategoryId}/{wordIndex}") { entry ->
                WordDetailScreen(
                    categoryId = entry.arguments?.getString("categoryId"),
                    subcategoryId = entry.arguments?.getString("subcategoryId"),
                    initialIndex = entry.arguments?.getString("wordIndex")?.toIntOrNull() ?: 0,
                    navController = navController,
                )
            }
            composable("score") { ScoreScreen(navController) }
            composable("favorites") { FavoritesScreen() }
            composable("progress") { ProgressScreen() }
            composable("settings") { SettingsScreen(navController) }
        }
    }
}

@Composable
private fun FloatingBottomNav(
    tabs: List<NavTab>,
    selectedRoute: String?,
    onTabSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(70.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF6E9579)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEach { tab ->
            NavTabItem(
                modifier = Modifier.weight(1f),
                iconRes = tab.iconRes,
                label = tab.label,
                isSelected = selectedRoute == tab.route,
                onClick = { onTabSelected(tab.route) },
            )
        }
    }
}

@Composable
private fun NavTabItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) { 
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = if (isSelected) Color(0xFFF9F3F2) else Color(0xFF5E6B5F),
                modifier = Modifier.size(22.dp),
            )
            if (isSelected) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = label,
                    color = Color(0xFFF9F3F2),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp,
                )
            }
        }
    }
}
