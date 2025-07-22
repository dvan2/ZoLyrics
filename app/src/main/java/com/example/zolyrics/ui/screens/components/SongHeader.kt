package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SongHeader(title: String, artist: String, bpm: Int, key: String) {
    Column (

    ){
        Text(text = title, style= MaterialTheme.typography.titleLarge)
        Text(text = "by $artist", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "BPM: $bpm", style = MaterialTheme.typography.bodySmall)
        Text(text = "Key: $key", style = MaterialTheme.typography.bodySmall)
    }
}