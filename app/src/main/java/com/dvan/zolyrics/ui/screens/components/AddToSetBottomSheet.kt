package com.dvan.zolyrics.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dvan.zolyrics.data.model.SongSet
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToSetBottomSheet(
    selectedSongId: String?,
    sets: List<SongSet>,
    songSetViewModel: SongSetViewModel,
    sheetState: SheetState,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    if (selectedSongId != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Text(
                "Add to Set",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            sets.forEach { set ->
                var songAlreadyInSet by remember { mutableStateOf(false) }

                LaunchedEffect(set.id, selectedSongId) {
                    songAlreadyInSet = songSetViewModel.isSongInSet(set.id, selectedSongId)
                }

                ListItem(
                    headlineContent = { Text(set.title) },
                    modifier = Modifier.clickable(enabled = !songAlreadyInSet) {
                        coroutineScope.launch {
                            songSetViewModel.addSongToSet(set.id, selectedSongId)
                            sheetState.hide()
                            onDismiss()
                            snackbarHostState.showSnackbar("Added to \"${set.title}\"")
                        }
                    },
                    supportingContent = if (songAlreadyInSet) {
                        { Text("Already in set", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    } else null
                )
            }
        }
    }
}
