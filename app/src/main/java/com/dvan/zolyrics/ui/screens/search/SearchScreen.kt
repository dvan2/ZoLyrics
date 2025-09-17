// Updated SearchScreen.kt to fix songIds error and disable duplicates using ViewModel check
package com.dvan.zolyrics.ui.screens.search

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dvan.zolyrics.ui.screens.components.AddToSetBottomSheet
import com.dvan.zolyrics.ui.screens.components.LocalSnackbarHostState
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onOpenSong: (String) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    songSetViewModel: SongSetViewModel = hiltViewModel()
) {
    val query = searchViewModel.query
    val results = searchViewModel.results
    val sets by songSetViewModel.allSets.collectAsState()

    var selectedSongId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = LocalSnackbarHostState.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = searchViewModel::onQueryChange,
            label = { Text("Search lyrics, title, or artist") },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { searchViewModel.onQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (query.isNotBlank()) {
            Text(
                text = "${results.size} result${if (results.size == 1) "" else "s"} found",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results, key = { it.song.id }) { result ->
                SearchResultItem(
                    result = result,
                    onClick = { onOpenSong(result.song.id) },
                    onLongClick = { selectedSongId = result.song.id }
                )
            }
        }
    }

    AddToSetBottomSheet(
        selectedSongId = selectedSongId,
        sets = sets,
        songSetViewModel = songSetViewModel,
        sheetState = sheetState,
        snackbarHostState = LocalSnackbarHostState.current,
        onDismiss = { selectedSongId = null }
    )
}

@Composable
fun SearchResultItem(result: SearchResult, onClick: () -> Unit, onLongClick: () -> Unit) {
    val song = result.song
    val matched = result.matchedLine

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 6.dp)
    ) {
        Text(song.title, style = MaterialTheme.typography.titleMedium)
        Text(song.artistName, style = MaterialTheme.typography.bodySmall)

        if (!song.key.isNullOrBlank() || song.bpm != null) {
            Text(
                listOfNotNull(
                    song.key?.let { "Key: $it" },
                    song.bpm?.let { "BPM: $it" }
                ).joinToString(" • "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!matched.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = matched,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
