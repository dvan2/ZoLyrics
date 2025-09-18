package com.dvan.zolyrics.ui.screens.sets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dvan.zolyrics.R
import com.dvan.zolyrics.ui.keys.KeyPickerSheet
import com.dvan.zolyrics.ui.model.SongInSetContract
import com.dvan.zolyrics.ui.navigation.Routes
import com.dvan.zolyrics.ui.screens.components.LocalSnackbarHostState
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel
import kotlinx.coroutines.launch

private val MUSICAL_KEYS_SHARPS = listOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDetailScreen(
    setId: String,
    viewModel: SongSetViewModel = hiltViewModel(),
    onSongClick: (String) -> Unit,
    navController: NavController
) {
    LaunchedEffect(setId) { viewModel.loadSongsForSet(setId) }

    val songsUi = viewModel.songsInSetUi.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    var editing by remember { mutableStateOf<SongInSetContract?>(null) }

    val haptics = LocalHapticFeedback.current

    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()

    when {
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        songsUi.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(
            stringResource(R.string.no_songs_in_playlist)
        ) }
        else -> {
            @Suppress("UNCHECKED_CAST")
            val typed: List<SongInSetContract> = songsUi as List<SongInSetContract>

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // List of songs in set
                Box(Modifier.weight(1f)) {
                    ReorderableSongList(
                        items = typed,
                        onMove = { from, to -> viewModel.moveItem(from, to) },
                        onDragEnd = { viewModel.persistSetOrder() },
                        haptics = haptics,
                        onSongClick = { clickedSongId ->
                            navController.navigate(Routes.setRunner(setId,clickedSongId))
                        },
                        onEditClick = { ui -> editing = ui }
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(Routes.setRunner(setId)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Set Runner")
                }
            }

        }
    }
    // Key picker (opened when editing != null)
    if (editing != null) {
        val songId = editing!!.songId
        val preferredKey = editing!!.preferredKey
        val originalKey = editing!!.originalKey ?: "N/A"

        KeyPickerSheet(
            currentPreferred = preferredKey,
            original = if (originalKey.isBlank()) "N/A" else originalKey,
            onPick = { key ->
                viewModel.setPreferredKeyForSong(songId, key)
                editing = null
            },
            onReset = {
                viewModel.clearPreferredKeyForSong(songId)
                editing = null
            },
            onClose = { editing = null },
            onDelete = {
                viewModel.removeSongFromSet(setId, songId)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Removed from set")
                }
                editing = null
            }
        )
    }
}
