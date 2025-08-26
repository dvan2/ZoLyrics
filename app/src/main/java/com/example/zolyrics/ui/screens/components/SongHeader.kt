package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.combinedClickable
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
import com.example.zolyrics.ui.theme.LocalSpacing

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

    val spacing = LocalSpacing.current

    Column {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text("by $artist", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(spacing.sm))
        val meta = buildString {
            (bpm ?: 0).takeIf { it > 0 }?.let { append("BPM $it") }
            val effective = globalPreferredKey ?: originalKey
            if (isNotEmpty()) append(" • ")
            append("Key: $effective")
            if (!originalKey.equals(effective, ignoreCase = true)) {
                append(" (orig. $originalKey)")
            }
        }

        Text(
            meta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .combinedClickable(
                    onClick = { showPicker = true },
                    onLongClick = onClearGlobalPreferredKey
                )
        )
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
