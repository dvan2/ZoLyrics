package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.ui.viewmodel.SongSetViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun SetDetailScreen(
    setId: String,
    viewModel: SongSetViewModel = viewModel(factory = SongSetViewModel.Factory),
    onSongClick: (String) -> Unit
) {
    // Trigger loading only once for this set
    LaunchedEffect(setId) {
        viewModel.loadSongsForSet(setId)
    }

    val songs by viewModel.currentSetSongs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        songs.isEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No songs found in this set.")
            }
        }

        else -> {
            LazyColumn {
                items(songs) { song ->
                    ListItem(
                        headlineContent = { Text(song.title) },
                        supportingContent = { Text(song.artist) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSongClick(song.id) }
                    )
                }
            }
        }
    }
}