package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.ui.screens.components.SongCard
import com.example.zolyrics.ui.viewmodel.SongSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val songsUi by viewModel.songsInSetUi.collectAsState()

    var editingSongId by remember { mutableStateOf<String?>(null) }
    val editingUi = songsUi.firstOrNull { it.song.id == editingSongId }

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
                items(songsUi) { ui ->
                    SongCard(
                        song = ui.song,
                        onClick = { onSongClick(ui.song.id)}
                    )
                    SongKeyRowCompact(
                        original = ui.originalKey,
                        preferred = ui.preferredKey,
                        onEditClick = { editingSongId = ui.song.id },
                    )
                }
            }
        }
    }

    if (editingSongId != null) {
        KeyPickerSheet(
            currentPreferred = editingUi?.preferredKey,
            original = editingUi?.originalKey ?: "N/A",
            onPick = { key ->
                viewModel.setPreferredKeyForSong(editingSongId!!, key)
                editingSongId = null
            },
            onReset = {
                viewModel.clearPreferredKeyForSong(editingSongId!!)
                editingSongId = null
            },
            onClose = { editingSongId = null }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyPickerSheet(
    currentPreferred: String?,        // null = using original
    original: String,
    onPick: (String) -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit
) {
    val keysSharps = listOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")

    ModalBottomSheet(onDismissRequest = onClose) {
        Column(Modifier.padding(16.dp)) {
            Text("Choose preferred key", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.padding(top = 12.dp))

            // Grid of keys
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(keysSharps.size) { idx ->
                    val k = keysSharps[idx]
                    val isSelected = currentPreferred == k
                    AssistChip(
                        onClick = { onPick(k) },
                        label = { Text(k) },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null)
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.padding(top = 8.dp))

            // Reset + original hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Original: $original",
                    style = MaterialTheme.typography.bodySmall
                )
                TextButton(onClick = onReset) { Text("Reset to original") }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onClose) { Text("Close") }
            }
        }
    }
}

@Composable
fun SongKeyRowCompact(
    original: String,
    preferred: String?,
    onEditClick: () -> Unit
) {
    val safeOriginal = if (original.isBlank()) "N/A" else original
    val playKey = preferred ?: safeOriginal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Key: $playKey",
                style = MaterialTheme.typography.bodyMedium
            )
            if (preferred != null && preferred != original) {
                Text(
                    text = "(orig. $safeOriginal)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        AssistChip(
            onClick = onEditClick,
            label = {
                Text("Edit")
            }
        )
    }
}

