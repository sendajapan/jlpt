package com.scholarlyapps.pathlingo.ui

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.scholarlyapps.pathlingo.R

private data class NavTab(val iconRes: Int, val label: String, val destId: Int)

class MainDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabs = listOf(
            NavTab(R.drawable.ic_home,     "Home",     R.id.nav_home),
            NavTab(R.drawable.ic_heart,    "Saved",    R.id.nav_favorites),
            NavTab(R.drawable.ic_chart,    "Progress", R.id.nav_progress),
            NavTab(R.drawable.ic_settings, "Settings", R.id.nav_settings),
        )

        setContent {
            MaterialTheme {
                DashboardScreen(activity = this, tabs = tabs)
            }
        }
    }
}

@Composable
private fun DashboardScreen(activity: AppCompatActivity, tabs: List<NavTab>) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var navController by remember { mutableStateOf<NavController?>(null) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val idx = tabs.indexOfFirst { it.destId == destination.id }
            if (idx >= 0) selectedTab = idx
        }
        navController?.addOnDestinationChangedListener(listener)
        onDispose { navController?.removeOnDestinationChangedListener(listener) }
    }

    Scaffold(
        containerColor = Color(0xFFF6EBD7),
        bottomBar = {
            FloatingBottomNav(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    val options = NavOptions.Builder()
                        .setPopUpTo(R.id.nav_home, false, true)
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .build()
                    navController?.navigate(tabs[index].destId, null, options)
                },
            )
        },
    ) { innerPadding ->
        val fm = activity.supportFragmentManager
        AndroidView(
            factory = { ctx ->
                FragmentContainerView(ctx).apply { id = R.id.fragment_container }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (fm.findFragmentById(R.id.fragment_container) == null) {
                val navHost = NavHostFragment.create(R.navigation.nav_dashboard)
                fm.beginTransaction()
                    .replace(R.id.fragment_container, navHost)
                    .setPrimaryNavigationFragment(navHost)
                    .commitNow()
                navController = navHost.navController
            }
        }
    }
}

@Composable
private fun FloatingBottomNav(
    tabs: List<NavTab>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
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
        tabs.forEachIndexed { index, tab ->
            NavTabItem(
                modifier = Modifier.weight(1f),
                iconRes = tab.iconRes,
                label = tab.label,
                isSelected = selectedTab == index,
                onClick = { onTabSelected(index) },
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
