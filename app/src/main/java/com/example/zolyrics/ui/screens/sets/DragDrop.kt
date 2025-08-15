package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

class DragDropState(
    val listState: LazyListState,
    val onMove: (from: Int, to: Int) -> Unit,
    val onDragEnd: () -> Unit
) {
    var draggedItemIndex: Int? = null
        internal set

    private var accumulatedDy = 0f

    var draggedKey by mutableStateOf<Any?>(null)
        internal set

    var targetIndex by mutableStateOf<Int?>(null)
        internal set

    fun onDragStart(index: Int) {
        draggedItemIndex = index
        targetIndex = index
        accumulatedDy = 0f
    }

    fun onDrag(dy: Float) {
        val currentIndex = draggedItemIndex ?: return
        accumulatedDy += dy
        val newIndex = indexAfterOffset(currentIndex, accumulatedDy)
        if (newIndex != null){
            targetIndex = newIndex
            if (newIndex != currentIndex) {
                onMove(currentIndex, newIndex)
                draggedItemIndex = newIndex
                accumulatedDy = 0f
            }

        }
    }

    fun onDragEndOrCancel() {
        draggedItemIndex = null
        accumulatedDy = 0f
        targetIndex = null
        onDragEnd()
    }

    private fun indexAfterOffset(startIndex: Int, dy: Float): Int? {
        val info = listState.layoutInfo
        val visible = info.visibleItemsInfo
        if (visible.isEmpty()) return null

        val startItem = visible.firstOrNull { it.index == startIndex } ?: return null

        val draggedCenterY = startItem.offset + startItem.size / 2f + dy

        val target = visible.minByOrNull { item ->
            val center = item.offset + item.size / 2f
            abs(draggedCenterY - center)
        } ?: return null

        return target.index
    }
}

internal fun Modifier.dragDrop(
    state: DragDropState
) = pointerInput(state) {
    detectDragGesturesAfterLongPress(
        onDragStart = { offset: Offset ->
            // Map pointer Y (relative to this LazyColumn) to an item index
            val visible = state.listState.layoutInfo.visibleItemsInfo
            val hit = visible.firstOrNull { item ->
                offset.y.toInt() in item.offset until (item.offset + item.size)
            }
            hit?.index?.let { state.onDragStart(it) }
        },
        onDrag = { _, dragAmount -> state.onDrag(dragAmount.y) },
        onDragEnd = { state.onDragEndOrCancel() },
        onDragCancel = { state.onDragEndOrCancel() }
    )
}
