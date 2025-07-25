package com.example.zolyrics.ui.screens.favorites

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.ui.screens.home.SongCard
import com.example.zolyrics.ui.screens.home.SongListScreen
import com.example.zolyrics.ui.viewmodel.SongViewModel

@Composable
fun FavoritesScreen (
    viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    onSongClick: (String) -> Unit = {}
    ,
){
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    val songs by viewModel.localSongs.collectAsState(initial = emptyList())

    val favoriteSongs = remember(favorites, songs) {
        songs.filter { song -> favorites.any { it.songId == song.id} }
    }

//    SongListScreen(
//        songs = favoriteSongs,
//    )
}