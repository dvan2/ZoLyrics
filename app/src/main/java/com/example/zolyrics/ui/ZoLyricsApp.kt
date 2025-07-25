package com.example.zolyrics.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.Alignment
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
import com.example.zolyrics.ui.viewmodel.LyricsUiState
import com.example.zolyrics.ui.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoLyricsApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)

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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (currentRoute?.startsWith("song/") == true) {
                        val currentSongId = currentBackStackEntry?.arguments?.getString("songId")
                        if (currentSongId != null) {
                            val isFavorite by viewModel.isFavorite(currentSongId).collectAsState(initial = false)
                            IconButton(onClick = {
                                viewModel.toggleFavorite(songId = currentSongId)
                            }) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite"
                                )
                            }
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
                FavoritesScreen(navController)
            }
            composable("song/{songId}") { backStackEntry->
                val songId = backStackEntry.arguments?.getString("songId")

                LaunchedEffect(songId) {
                    if (songId != null) {
                        viewModel.loadLyrics(songId)
                    }
                }

                val songList = viewModel.localSongs.collectAsState(initial = emptyList()).value
                val song = songList.find { it.id == songId }
                val lyricsState = viewModel.lyricsState.collectAsState().value

                when {
                    song == null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    lyricsState is LyricsUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    lyricsState is LyricsUiState.Success -> {
                        SongScreen(song = song, lyrics = lyricsState.lyrics)
                    }

                    lyricsState is LyricsUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${lyricsState.message}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == Screen.Home.route,
            onClick = { onTabSelected(Screen.Home.route) },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home")
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