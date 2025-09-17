package com.dvan.zolyrics.ui.keys

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dvan.zolyrics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyPickerSheet(
    currentPreferred: String?,
    original: String,
    onPick: (String) -> Unit,
    onReset: () -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
) {
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
                items(MUSICAL_KEYS_SHARPS.size) { idx ->
                    val k = MUSICAL_KEYS_SHARPS[idx]
                    val isSelected = currentPreferred == k
                    AssistChip(
                        onClick = { onPick(k) },
                        label = { Text(k) },
                        leadingIcon = { if (isSelected) Icon(Icons.Default.Check, contentDescription = null) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

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

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.remove_from_playlist))
            }

            Spacer(Modifier.height(8.dp))


            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onClose) { Text("Close") }
            }
        }
    }
}
