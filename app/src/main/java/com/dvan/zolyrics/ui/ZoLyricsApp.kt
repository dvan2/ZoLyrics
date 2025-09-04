package com.dvan.zolyrics.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dvan.zolyrics.ui.navigation.Screen
import com.dvan.zolyrics.ui.navigation.ZoLyricsNavHost
import com.dvan.zolyrics.ui.screens.components.ZoLyricsTopBar
import com.dvan.zolyrics.ui.viewmodel.SongViewModel

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
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            ZoLyricsTopBar(currentRoute, navController, viewModel)
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentRoute ?: Screen.Home.route,
                navController = navController,
            )
        },
        floatingActionButton = {
            FabForRoute(
                currentRoute = currentRoute,
                navController = navController
            )
        }
    ){ innerPadding ->
        ZoLyricsNavHost(navController, innerPadding, viewModel, setFabIcon = { fabIcon = it} , setFabClick = { onFabClick = it})
    }
}

@Composable
fun FabForRoute(currentRoute: String?, navController: NavHostController) {
    when (currentRoute) {
        // Show an “add” FAB on the sets list
        Screen.Sets.route -> {
            FloatingActionButton(onClick = { navController.navigate("sets/create") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Set")
            }
        }

        // On create screen, trigger a save event via SavedStateHandle
        "sets/create" -> {
            FloatingActionButton(onClick = {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    // bump a timestamp to signal an event
                    ?.set("fab_save_request", System.currentTimeMillis())
            }) {
                Icon(Icons.Filled.Check, contentDescription = "Save Set")
            }
        }

        // Default: no FAB
        else -> Unit
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: String,
//    onTabSelected: (String) -> Unit,
    navController: NavController
) {
    val bottomBarScreens = listOf(Screen.Home, Screen.Favorites, Screen.Sets)


    NavigationBar {
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = selectedTab == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        if (screen == Screen.Sets) {
                            popUpTo(Screen.Sets.route) { inclusive = true }
                        } else {
                            popUpTo(screen.route) { inclusive = true}
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}