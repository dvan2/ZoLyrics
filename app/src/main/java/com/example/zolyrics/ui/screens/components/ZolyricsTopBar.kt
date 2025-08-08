package com.example.zolyrics.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoLyricsTopBar(
    currentRoute: String?,
    navController: NavHostController,
    viewModel: SongViewModel
) {
    TopAppBar(
        title = {
            Text(
                when {
                    currentRoute == Screen.Home.route -> "ZoLyrics"
                    currentRoute == Screen.Favorites.route -> "Favorites"
                    currentRoute == Screen.Sets.route -> "Sets"
                    currentRoute == "sets/create" -> "Create Set"
                    currentRoute?.startsWith("sets/") == true -> "Set Details"
                    currentRoute?.startsWith("song/") == true -> "Song"
                    else -> ""
                }
            )
        },
        navigationIcon = {
            val isTopLevel = currentRoute == Screen.Home.route ||
                    currentRoute == Screen.Favorites.route ||
                    currentRoute == Screen.Sets.route
            if (!isTopLevel) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (currentRoute?.startsWith("song/") == true) {
                val currentSongId = navController.currentBackStackEntry?.arguments?.getString("songId")
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
}
