//package com.example.zolyrics.ui.screens.components
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.zolyrics.data.model.Song
//import com.example.zolyrics.ui.theme.LocalElevations
//import com.example.zolyrics.ui.theme.LocalSpacing
//import com.example.zolyrics.ui.viewmodel.PreferredKeyViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SongCard(song: Song, onClick: () -> Unit) {
//    val keyVm: PreferredKeyViewModel = viewModel(factory = PreferredKeyViewModel.Factory)
//    val prefMap by keyVm.map.collectAsState()
//    val effectiveKey = prefMap[song.id] ?: song.key ?: ""
//
//    val spacing = LocalSpacing.current
//    val elevations = LocalElevations.current
//    val colors = MaterialTheme.colorScheme
//
//    Card(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = spacing.lg, vertical = spacing.sm),
//        shape = MaterialTheme.shapes.large,
//        elevation = CardDefaults.cardElevation(elevations.level2),
//        colors = CardDefaults.cardColors(containerColor = colors.surface)
//    ) {
//        Column(Modifier.padding(spacing.lg)) {
//            Text(
//                text = song.title,
//                style = MaterialTheme.typography.titleMedium,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                color = colors.onSurface
//            )
//            Text(
//                text = song.artistName,
//                style = MaterialTheme.typography.bodyMedium,
//                color = colors.onSurfaceVariant
//            )
//
//            if (effectiveKey.isNotBlank() && effectiveKey != "N/A") {
//                AssistChip(
//                    onClick = { /* no-op: read-only on Home */ },
//                    label = { Text(effectiveKey) },
//                    enabled = false   // disables interaction style
//                )
//            }
//        }
//    }
//}

package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.viewmodel.PreferredKeyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongCard(
    song: Song,
    onClick: () -> Unit
) {
    val keyVm: PreferredKeyViewModel = viewModel(factory = PreferredKeyViewModel.Factory)
    val prefMap by keyVm.map.collectAsState()
    val originalKey = song.key.orEmpty()
    val effectiveKey = prefMap[song.id]?.takeIf { it.isNotBlank() } ?: originalKey

    val colors = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading avatar / placeholder art
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                color = colors.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.onSurface
                )
                Text(
                    text = song.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Show a subtle hint if user preference transposes away from the original
                if (effectiveKey.isNotBlank() && originalKey.isNotBlank() && !effectiveKey.equals(originalKey, true)) {
                    Text(
                        text = "Transposed from $originalKey",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (effectiveKey.isNotBlank()) {
                    AssistChip(
                        onClick = { /* read-only */ },
                        enabled = false,
                        label = { Text(effectiveKey) }
                    )
                    Spacer(Modifier.width(6.dp))
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant
                )
            }
        }
    }
}
