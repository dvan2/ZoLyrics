package com.example.zolyrics.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.navigation.Screen
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
        viewModel = viewModel,
        navController = navController,
    )
}


@Composable
fun SongListScreen(
    songs: List<Song>,
    viewModel: SongViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    LazyColumn ( modifier = modifier){
        items(songs) { song ->
            SongCard(song, onClick = {
                viewModel.loadLyrics(song.id)
                navController.navigate(Screen.SongDetail.createRoute(song.id))
            })
        }
    }
}


@Composable
fun SongCard(song: Song,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = song.artist,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )
    }

}
