package com.dvan.zolyrics.ui.screens.sets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dvan.zolyrics.ui.screens.components.SongScreen
import com.dvan.zolyrics.ui.viewmodel.LyricsUiState
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel
import com.dvan.zolyrics.ui.viewmodel.SongViewModel

@Composable
fun SetRunnerScreen(
    setId: String,
    startSongId: String? = null,
    songSetViewModel: SongSetViewModel = hiltViewModel(),
    songViewModel: SongViewModel = hiltViewModel(),
    onExit: () -> Unit
) {
    // Load songs in set
    LaunchedEffect(setId) { songSetViewModel.loadSongsForSet(setId) }

    val songsUi by songSetViewModel.songsInSetUi.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }

    if (songsUi.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No songs in this set")
        }
        return
    }

    LaunchedEffect(songsUi, startSongId) {
        if(songsUi.isNotEmpty() && startSongId !=  null) {
            val idx = songsUi.indexOfFirst { it.song.id == startSongId }
            if ( idx>= 0) currentIndex = idx
        }
    }

    val currentSong = songsUi[currentIndex].song

    // Load lyrics when song changes
    LaunchedEffect(currentSong.id) {
        songViewModel.loadLyrics(currentSong.id)
    }
    val lyrics by songViewModel.lyricsState.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            when (lyrics) {
                is LyricsUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is LyricsUiState.Error -> Text(
                    (lyrics as LyricsUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                is LyricsUiState.Success -> {
                    val lines = (lyrics as LyricsUiState.Success).lyrics
                    SongScreen(song = currentSong, lyrics = lines,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Divider()
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentIndex > 0) {
                FilledTonalButton(onClick = { currentIndex-- }) { Text("Previous") }
            } else Spacer(Modifier.width(96.dp))

            Text("${currentIndex + 1} / ${songsUi.size}", style = MaterialTheme.typography.labelLarge)

            if (currentIndex < songsUi.size - 1) {
                FilledTonalButton(onClick = { currentIndex++ }) { Text("Next") }
            } else {
                FilledTonalButton(onClick = onExit) { Text("Finish") }
            }
        }
    }


}
