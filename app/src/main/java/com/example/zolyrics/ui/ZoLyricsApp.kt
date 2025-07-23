package com.example.zolyrics.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.screens.SongScreen
import com.example.zolyrics.ui.screens.favorites.FavoritesScreen
import com.example.zolyrics.ui.screens.home.HomeScreen
import com.example.zolyrics.ui.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoLyricsApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            Screen.Home.route -> "ZoLyrics"
                            Screen.Favorites.route -> "Favorites"
                            else -> "Song"
                        }
                    )
                },
                navigationIcon = {
                    if (currentRoute?.startsWith("song/") == true) {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentRoute ?: Screen.Home.route,
                onTabSelected = { navController.navigate(it) }
            )
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route){
                HomeScreen(navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable("song/{songId}") { backStackEntry->
                val songId = backStackEntry.arguments?.getString("songId")
                val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)

                val songList = viewModel.localSongs.collectAsState(initial = emptyList()).value
                val song = songList.find { it.id == songId }
                val lyrics = viewModel.selectedLyrics.collectAsState().value

                LaunchedEffect(songId) {
                    viewModel.loadLyrics(songId ?: "")
                }

                song?.let {
                    SongScreen(song = it, lyrics = lyrics)
                }

            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar (

    ){
        NavigationBarItem(
            selected = selectedTab == Screen.Home.route,
            onClick = { onTabSelected(Screen.Home.route) },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home",)
            },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selectedTab == Screen.Favorites.route,
            onClick = { onTabSelected(Screen.Favorites.route)},
            icon = {
                Icon(Icons.Default.Favorite, contentDescription = "Favorites")
            },
            label = { Text("Favorites")}
        )
    }
}