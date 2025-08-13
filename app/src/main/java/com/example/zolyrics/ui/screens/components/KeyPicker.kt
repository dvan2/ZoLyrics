package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable

@Composable
fun KeyPicker(
    current: String?,
    onSelect: (String) -> Unit,
    onResetToOriginal: (() -> Unit)? = null,
    dismiss: () -> Unit
) {
    val allKeys = listOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")
    // BottomSheetScaffold or ModalBottomSheet works great. Here’s a lightweight Column:
    androidx.compose.material3.AlertDialog(
        onDismissRequest = dismiss,
        title = { androidx.compose.material3.Text("Choose preferred key") },
        text = {
            FlowRow {
                allKeys.forEach { k ->
                    androidx.compose.material3.AssistChip(
                        onClick = { onSelect(k); dismiss() },
                        label = { androidx.compose.material3.Text(k) },
                        leadingIcon = if (k == current) ({ androidx.compose.material3.Text("✓") }) else null
                    )
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = dismiss) {
                androidx.compose.material3.Text("Close")
            }
        },
        dismissButton = if (onResetToOriginal != null) {
            {
                androidx.compose.material3.TextButton(onClick = { onResetToOriginal(); dismiss() }) {
                    androidx.compose.material3.Text("Reset to original")
                }
            }
        } else null
    )
}
