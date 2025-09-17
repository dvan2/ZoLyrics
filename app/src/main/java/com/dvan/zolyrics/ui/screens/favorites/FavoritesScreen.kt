package com.dvan.zolyrics.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dvan.zolyrics.ui.navigation.Screen
import com.dvan.zolyrics.ui.screens.home.SongListScreen
import com.dvan.zolyrics.ui.viewmodel.SongViewModel

@Composable
fun FavoritesScreen (
    navController: NavController,
){
    val viewModel: SongViewModel = hiltViewModel()
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
        },
        onSongLongClick = null
    )
}