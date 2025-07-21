package com.example.zolyrics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zolyrics.ui.theme.ZoLyricsTheme
import com.example.zolyrics.ui.viewmodel.SongViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongWithLyrics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZoLyricsTheme {
                ZoLyricsApp()
            }
        }
    }
}

@Composable
private fun ZoLyricsApp() {
    val viewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
    val songsState = viewModel.localSongs.collectAsState(initial = emptyList())
    val songs = songsState.value
    val lyricsState = viewModel.selectedLyrics.collectAsState()
    val lyrics = lyricsState.value

    Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
        SongListScreen(
            songs = songs,
            lyrics = lyrics,
            onSongClick = {id -> viewModel.loadLyrics(id)},
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SongListScreen(
    songs: List<Song>,
    lyrics: List<LyricLine>,
    onSongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text("Hello")
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
        Text(text = song.title, style = MaterialTheme.typography.bodyLarge)
        Text(text = "by ${song.artist}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Tap to view lyrics", modifier = Modifier.padding(top = 4.dp))
        lyrics.forEach { line ->
            Text(text = "${line.sectionType}: ${line.content}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
