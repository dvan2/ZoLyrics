package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    val effective = globalPreferredKey ?: originalKey
    val overridden = !globalPreferredKey.isNullOrBlank() && globalPreferredKey != originalKey

    Column {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text("by $artist", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Text("BPM: $bpm", style = MaterialTheme.typography.bodySmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Key: $effective",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .combinedClickable(
                        onClick = { showPicker = true },
                        onLongClick = onClearGlobalPreferredKey  // long-press = reset
                    )
            )
            Text("(orig. $originalKey)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
