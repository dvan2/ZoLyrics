package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.viewmodel.PreferredKeyViewModel

@Composable
fun SongCard(song: Song,onClick: () -> Unit) {
    val keyVm: PreferredKeyViewModel = viewModel(factory = PreferredKeyViewModel.Factory)

    val prefMap by keyVm.map.collectAsState()
    val globalPref = prefMap[song.id]
    val originalKey = song.key ?: "N/A"
    val effectiveKey = globalPref ?: originalKey

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                onClick = onClick,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.artistName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (effectiveKey.isNotBlank() && effectiveKey != "N/A") {
                    AssistChip(
                        onClick = { /* no-op: read-only on Home */ },
                        label = { Text(effectiveKey) },
                        enabled = false   // disables interaction style
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

//            // Optional: show original when overridden, also read-only
//            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                if (song.bpm != null && song.bpm > 0) {
//                    AssistChip(onClick = {}, label = { Text("BPM ${song.bpm}") }, enabled = false)
//                }
//                if (overridden) {
//                    AssistChip(onClick = {}, label = { Text("orig $originalKey") }, enabled = false)
//                }
//            }
        }
    }
}
