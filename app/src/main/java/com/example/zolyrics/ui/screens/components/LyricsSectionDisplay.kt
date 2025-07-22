package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zolyrics.data.model.LyricLine

@Composable
fun LyricsSectionDisplay(sectionTitle: String, lines: List<LyricLine>) {
    Column (
        modifier = Modifier.padding(vertical = 8.dp)
    ){
        Text(
            text = sectionTitle.uppercase(),
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        lines.forEach { line ->
            Text(text = line.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}