package com.example.zolyrics.ui.screens.sets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.ui.screens.components.SongListItem
import com.example.zolyrics.ui.viewmodel.SongSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDetailScreen(
    setId: String,
    viewModel: SongSetViewModel = viewModel(factory = SongSetViewModel.Factory),
    onSongClick: (String) -> Unit
) {
    LaunchedEffect(setId) {
        viewModel.loadSongsForSet(setId)
    }

    val songsUi by viewModel.songsInSetUi.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var editingSongId by remember { mutableStateOf<String?>(null) }
    val editingUi = songsUi.firstOrNull { it.song.id == editingSongId }

    val listState = rememberLazyListState()

    val dragState = remember(listState, viewModel) {
        DragDropState(
            listState = listState,
            onMove = { from, to -> viewModel.moveItem(from, to) },
            onDragEnd = {
                viewModel.persistSetOrder()
            }

        )
    }

    val haptics = LocalHapticFeedback.current
    val anyDragging = dragState.draggedItemIndex != null

    LaunchedEffect(anyDragging) {
        if (!anyDragging) {
            // drag just ended
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        songsUi.isEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No songs found in this set.")
            }
        }

        else -> {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .dragDrop(dragState)
            ) {
                itemsIndexed(
                    items = songsUi,
                    key = { _, ui -> ui.song.id }
                ) { index, ui ->
                    val isDragging = index == dragState.draggedItemIndex
                    val scale by animateFloatAsState(if (isDragging) 1.04f else 1f, label = "drag-scale")
                    val elevation by animateDpAsState(if (isDragging) 14.dp else 2.dp, label = "drag-elev")
                    val bg by animateColorAsState(
                        if (isDragging) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surface,
                        label = "drag-bg"
                    )
                    val borderColor = MaterialTheme.colorScheme.primary
                    val borderStroke = if (isDragging) BorderStroke(2.dp, borderColor) else null

                    val placeholderHeight by animateDpAsState(
                        if (index == dragState.targetIndex && !isDragging) 10.dp else 0.dp,
                        label = "gap"
                    )

                    val haptics = LocalHapticFeedback.current

                    LaunchedEffect(isDragging) {
                        if (isDragging) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }

                    LaunchedEffect(dragState.targetIndex) {
                        if (isDragging && dragState.targetIndex != null) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                    }

                    // ✅ Show the placeholder gap above the card
                    Spacer(Modifier.height(placeholderHeight))

                    Card(
                        border = borderStroke,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                            .zIndex(if (isDragging) 1f else 0f)
                            .animateItem(
                                fadeInSpec = null,
                                fadeOutSpec = null,
                                placementSpec = spring(stiffness = 300f)
                            )
                            .then(
                                if (isDragging) Modifier.shadow(8.dp, RoundedCornerShape(12.dp))
                                else Modifier
                            ),
                        colors = CardDefaults.cardColors()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Drag to reorder",
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .graphicsLayer(
                                        rotationZ = if (isDragging) 2f else 0f,
                                        scaleX = if (isDragging) 1.15f else 1f,
                                        scaleY = if (isDragging) 1.15f else 1f
                                    ),
                                tint = if (isDragging) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            SongListItem(
                                song = ui.song,
                                original = ui.originalKey,
                                preferred = ui.preferredKey,
                                onSongClick = { onSongClick(ui.song.id) },
                                onEditClick = { editingSongId = ui.song.id },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
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

            Spacer(Modifier.height(12.dp))

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
            .padding(top = 6.dp),
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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