package com.dvan.zolyrics.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dvan.zolyrics.data.model.Song

@Composable
fun SongListItem(
    song: Song,
    original: String,
    preferred: String?,
    onSongClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .padding(end = 8.dp)
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )
        Text(
            text = "by ${song.artistName}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        SongKeyRowCompact(
            original = original,
            preferred = preferred,
            onEditClick = onEditClick
        )
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