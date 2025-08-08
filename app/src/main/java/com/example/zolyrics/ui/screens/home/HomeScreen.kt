package com.example.zolyrics.ui.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.screens.components.SongCard
import com.example.zolyrics.ui.viewmodel.SongViewModel


@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
    val songsState = viewModel.localSongs.collectAsState(initial = emptyList())
    val songs = songsState.value

    SongListScreen(
        songs = songs,
        onSongClick = { id ->
            viewModel.loadLyrics(id)
            navController.navigate(Screen.SongDetail.createRoute(id))
        }
    )
}


@Composable
fun SongListScreen(
    songs: List<Song>,
    onSongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn ( modifier = modifier){
        items(songs) { song ->
            SongCard(song, onClick = {
                onSongClick(song.id)
            })
        }
    }
}


