package com.example.zolyrics.ui.screens.sets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.zolyrics.ui.model.SongInSetContract
import com.example.zolyrics.ui.screens.components.SongListItem


/**
 * Reorderable list responsible for: drag state, animations, haptics during drag, and rendering rows.
 *
 * NOTE: This expects the project to provide DragDropState + dragDrop(modifier) as in your current codebase.
 */
@Composable
fun <T : SongInSetContract> ReorderableSongList(
    items: List<T>,
    onMove: (from: Int, to: Int) -> Unit,
    onDragEnd: () -> Unit,
    haptics: HapticFeedback,
    onSongClick: (String) -> Unit,
    onEditClick: (T) -> Unit,
) {
    val listState = rememberLazyListState()

    val dragState = remember(listState, onMove, onDragEnd) {
        DragDropState(
            listState = listState,
            onMove = onMove,
            onDragEnd = onDragEnd
        )
    }

    // Single place to emit end-of-drag haptic
    val anyDragging = dragState.draggedItemIndex != null
    LaunchedEffect(anyDragging) {
        if (!anyDragging) haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .dragDrop(dragState)
    ) {
        itemsIndexed(
            items = items,
            key = { _, ui -> ui.songId }
        ) { index, ui ->
            val isDragging = index == dragState.draggedItemIndex

            val scale by animateDraggingScale(isDragging)
            val elevation by animateDraggingElevation(isDragging)

            val placeholderHeight by animateDpAsState(
                if (index == dragState.targetIndex && !isDragging) 10.dp else 0.dp,
                label = "gap"
            )

            // Per-item haptics: start + target move
            LaunchedEffect(isDragging) {
                if (isDragging) haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            LaunchedEffect(dragState.targetIndex) {
                if (isDragging && dragState.targetIndex != null) {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
            }

            Spacer(Modifier.height(placeholderHeight))

            Card(
                border = if (isDragging) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .zIndex(if (isDragging) 1f else 0f)
                    .animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null,
                        placementSpec = spring(stiffness = 300f)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
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
                        original = ui.originalKey ?: "N/A",
                        preferred = ui.preferredKey,
                        onSongClick = { onSongClick(ui.songId) },
                        onEditClick = { onEditClick(ui) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/** Small animation helpers to keep the call-sites clean */
@Composable
private fun animateDraggingScale(isDragging: Boolean): State<Float> =
    animateFloatAsState(if (isDragging) 1.04f else 1f, label = "drag-scale")

@Composable
private fun animateDraggingElevation(isDragging: Boolean): State<Dp> =
    animateDpAsState(if (isDragging) 14.dp else 2.dp, label = "drag-elev")
