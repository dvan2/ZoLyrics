package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.viewmodel.SongSetViewModel
import com.example.zolyrics.ui.viewmodel.SongViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetScreen(
    viewModel: SongSetViewModel = viewModel(factory = SongSetViewModel.Factory),
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    onSetCreated: () -> Unit,
    fabSaveRequests: StateFlow<Long>,
) {
    val allSongs by songViewModel.localSongs.collectAsState(initial = emptyList())
    var selectedSongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var setTitle      by remember { mutableStateOf("") }

    val trigger = fabSaveRequests.collectAsState().value
    LaunchedEffect(trigger) {
        if (trigger != 0L && setTitle.isNotBlank() && selectedSongs.isNotEmpty()) {
            viewModel.createSet(setTitle, selectedSongs)
            onSetCreated()
        }
    }


    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value       = setTitle,
            onValueChange = { setTitle = it },
            label       = { Text("Set Title") },
            modifier    = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Songs", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(allSongs) { song ->
                val isSelected = song in selectedSongs
                ListItem(
                    headlineContent  = { Text(song.title) },
                    supportingContent = { Text(song.artist) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedSongs =
                                if (isSelected) selectedSongs - song
                                else             selectedSongs + song
                        }
                        .padding(vertical = 4.dp),
                    trailingContent  = {
                        if (isSelected) Icon(Icons.Default.Check, contentDescription = null)
                    }
                )
            }
        }
    }
}
