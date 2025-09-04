package com.dvan.zolyrics.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.ui.viewmodel.SongViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onOpenSong: (String) -> Unit,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
) {
    val allSongs by songViewModel.localSongs.collectAsState(initial = emptyList())

    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Song>>(emptyList()) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    // Simple debounce on query changes
    LaunchedEffect(query, allSongs) {
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(250) // debounce window
            results = if (query.isBlank()) {
                emptyList()
            } else {
                val q = query.trim().lowercase()
                allSongs.filter { s ->
                    s.title.lowercase().contains(q)
                }.take(100)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (query.isBlank()) "Search" else "Search (${results.size})"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // Search field
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by title or artist") },
                singleLine = true,
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                }
            )

            // Results
            if (query.isNotBlank() && results.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text("No matches. Try a different word or check spelling.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(results) { song ->
                        ResultRow(
                            song = song,
                            onClick = { onOpenSong(song.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(
    song: Song,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
//        Text(
//            text = song.artist,
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
        // Tiny metadata row (optional): key/bpm
        // if you have them on Song, show them here.
    }
    Divider()
}
