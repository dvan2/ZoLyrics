package com.example.zolyrics.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.screens.SongScreen
import com.example.zolyrics.ui.viewmodel.SongViewModel


@Composable
fun HomeScreen(

) {
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
    val songsState = viewModel.localSongs.collectAsState(initial = emptyList())
    val songs = songsState.value
    val lyricsState = viewModel.selectedLyrics.collectAsState()
    val lyrics = lyricsState.value

    SongListScreen(
        songs = songs,
        lyrics = lyrics,
        onSongClick = {id -> viewModel.loadLyrics(id)},
    )
}


@Composable
fun SongListScreen(
    songs: List<Song>,
    lyrics: List<LyricLine>,
    onSongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn ( modifier = modifier){
        items(songs) { song ->
            SongCard(song, onClick = { onSongClick(song.id)}, lyrics = lyrics)
        }
    }
}


@Composable
fun SongCard(song: Song, lyrics: List<LyricLine>, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable( onClick = onClick)
    )
    {
        SongScreen(song= song ,lyrics = lyrics,)
    }
}
