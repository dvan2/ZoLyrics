package com.dvan.zolyrics.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dvan.zolyrics.ui.theme.LocalSpacing

@Composable
fun SongHeaderWithPref(
    title: String,
    artist: String,
    bpm: Int?,
    originalKey: String,
    globalPreferredKey: String?,
    onSetGlobalPreferredKey: (String) -> Unit,
    onClearGlobalPreferredKey: () -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Column {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text("by $artist", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(LocalSpacing.current.md))

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            bpm?.takeIf { it > 0 }?.let {
                AssistChip(
                    onClick = { /* Non-interactive for now */ },
                    label = { Text("BPM $it") }
                )
            }

            val effective = globalPreferredKey ?: originalKey
            AssistChip(
                onClick = { showPicker = true },
                label = {
                    Text(
                        if (!originalKey.equals(effective, ignoreCase = true)) {
                            "Key: $effective (orig. $originalKey)"
                        } else {
                            "Key: $effective"
                        }
                    )
                }
            )
        }

    }

    if (showPicker) {
        KeyPickerDialog(
            current = globalPreferredKey,
            onSelect = { onSetGlobalPreferredKey(it) },
            onResetToOriginal = onClearGlobalPreferredKey,
            onDismiss = { showPicker = false }
        )
    }
}

@Composable
private fun KeyPickerDialog(
    current: String?,
    onSelect: (String) -> Unit,
    onResetToOriginal: () -> Unit,
    onDismiss: () -> Unit
) {
    val keys = listOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose preferred key") },
        text = {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                keys.forEach { k ->
                    AssistChip(
                        onClick = { onSelect(k); onDismiss() },
                        label = { Text(if (k == current) "✓ $k" else k) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        dismissButton = {
            TextButton(onClick = { onResetToOriginal(); onDismiss() }) { Text("Reset") }
        }
    )
}
