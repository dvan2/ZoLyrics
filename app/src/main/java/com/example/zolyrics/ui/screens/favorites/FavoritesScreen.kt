package com.example.zolyrics.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.screens.home.SongListScreen
import com.example.zolyrics.ui.viewmodel.SongViewModel

@Composable
fun FavoritesScreen (
    navController: NavController,
){
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    val songs by viewModel.localSongs.collectAsState(initial = emptyList())

    val favoriteSongs = remember(favorites, songs) {
        songs.filter { song -> favorites.any { it.songId == song.id} }
    }

    SongListScreen(
        songs = favoriteSongs,
        onSongClick = { id ->
            viewModel.loadLyrics(id)
            navController.navigate(Screen.SongDetail.createRoute(id))
        }
    )
}