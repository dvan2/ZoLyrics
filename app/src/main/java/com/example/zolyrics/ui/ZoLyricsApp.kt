package com.example.zolyrics.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.navigation.ZoLyricsNavHost
import com.example.zolyrics.ui.screens.SongScreen
import com.example.zolyrics.ui.screens.components.ZoLyricsTopBar
import com.example.zolyrics.ui.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoLyricsApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)

    var onFabClick: (() -> Unit)? by remember { mutableStateOf(null) }
    var fabIcon: ImageVector? by remember { mutableStateOf(null) }

    Scaffold (
        topBar = {
            ZoLyricsTopBar(currentRoute, navController, viewModel)
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentRoute ?: Screen.Home.route,
                onTabSelected = { route ->
                    if (route == Screen.Sets.route) {
                        val popped = navController.popBackStack(route, false)
                        if (!popped) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true}
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } else {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            fabIcon?.let { icon ->
                IconButton(onClick = { onFabClick?.invoke() }) {
                    Icon(icon, contentDescription = "FAB Action")
                }
            }
        }
    ){ innerPadding ->
        ZoLyricsNavHost(navController, innerPadding, viewModel, setFabIcon = { fabIcon = it} , setFabClick = { onFabClick = it})
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val bottomBarScreens = listOf(Screen.Home, Screen.Favorites, Screen.Sets)

    NavigationBar {
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = selectedTab.startsWith(screen.route) == true,
                onClick = {
                    onTabSelected(screen.route)
                }
            )
        }
    }
}