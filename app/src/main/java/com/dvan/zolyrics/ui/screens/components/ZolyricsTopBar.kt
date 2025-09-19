package com.dvan.zolyrics.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dvan.zolyrics.R
import com.dvan.zolyrics.ui.navigation.Screen
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel
import com.dvan.zolyrics.ui.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoLyricsTopBar(
    currentRoute: String?,
    navController: NavHostController,
    viewModel: SongViewModel,
    songSetViewModel: SongSetViewModel
) {
    TopAppBar(
        title = {
            val titleText: String = when {
                currentRoute == Screen.Home.route -> stringResource(R.string.app_name)
                currentRoute == Screen.Favorites.route -> stringResource(R.string.label_favorites)
                currentRoute == Screen.Sets.route -> stringResource(R.string.label_sets)
                currentRoute == "sets/create" -> stringResource(R.string.label_create_set)

                // Set detail / runner: show set name if we have it
                currentRoute?.startsWith("sets/") == true -> {
                    val setId = navController.currentBackStackEntry?.arguments?.getString("setId")
                    if (setId != null) {
                        val setTitle by songSetViewModel.setTitle.collectAsState()
                        setTitle ?: stringResource(R.string.label_set_details)
                    } else {
                        stringResource(R.string.label_set_details)
                    }
                }

                currentRoute?.startsWith("song/") == true -> stringResource(R.string.label_song)
                currentRoute == Screen.Search.route -> stringResource(R.string.label_search)
                else -> stringResource(R.string.app_name)
            }

            Text(titleText)
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
            when {
                currentRoute?.startsWith("song/") == true -> {
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

                // Everywhere except the Search screen → show Search button
                currentRoute != Screen.Search.route -> {
                    IconButton(onClick = {
                        navController.navigate(Screen.Search.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            }
        }
    )
}
