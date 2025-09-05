package com.dvan.zolyrics.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dvan.zolyrics.ui.screens.SongScreen
import com.dvan.zolyrics.ui.screens.favorites.FavoritesScreen
import com.dvan.zolyrics.ui.screens.home.HomeScreen
import com.dvan.zolyrics.ui.screens.search.SearchScreen
import com.dvan.zolyrics.ui.screens.search.SearchViewModel
import com.dvan.zolyrics.ui.screens.sets.CreateSetScreen
import com.dvan.zolyrics.ui.screens.sets.SetDetailScreen
import com.dvan.zolyrics.ui.screens.sets.SetListScreen
import com.dvan.zolyrics.ui.viewmodel.LyricsUiState
import com.dvan.zolyrics.ui.viewmodel.SongViewModel

@Composable
fun ZoLyricsNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: SongViewModel,
    setFabIcon: (ImageVector?) -> Unit,
    setFabClick: ((() -> Unit)?) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable("song/{songId}") { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId")

            LaunchedEffect(songId) {
                if (songId != null) {
                    viewModel.loadLyrics(songId)
                }
            }

            val songs = viewModel.localSongs.collectAsState(initial = emptyList()).value
            val song = songs.find { it.id == songId }
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

        composable(Screen.Sets.route) {
            setFabIcon(Icons.Default.Add)
            setFabClick { navController.navigate("sets/create") }
            SetListScreen(
                navController = navController
            )
        }

        composable("sets/create") { backStackEntry ->
            val saveRequests = backStackEntry.savedStateHandle
                .getStateFlow("fab_save_request", 0L)
            var onSaveClick: (() -> Unit)? = null
            setFabIcon(Icons.Default.Check)
            setFabClick { onSaveClick?.invoke() }
            CreateSetScreen(
                fabSaveRequests = saveRequests,
                onSetCreated = {
                    navController.popBackStack()
                    navController.navigate(Screen.Sets.route){
                        launchSingleTop = true
                        popUpTo(Screen.Sets.route) { inclusive = true}
                    } },
            )
        }

        composable("sets/{setId}") { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: return@composable

            SetDetailScreen(
                setId = setId,
                onSongClick = { songId ->
                    navController.navigate(Screen.SongDetail.createRoute(songId))
                }
            )
        }
        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
            SearchScreen(
                onBack = { navController.popBackStack() },
                onOpenSong = { songId ->
                    navController.navigate("song/$songId")
                },
                searchViewModel= searchViewModel
            )
        }


    }
}
