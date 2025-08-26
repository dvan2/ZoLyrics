package com.example.zolyrics.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zolyrics.data.model.LyricLine

@Composable
fun LyricsSectionDisplay(sectionTitle: String, lines: List<LyricLine>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            .padding(12.dp)
    ) {
        Text(
            text = sectionTitle.uppercase(),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(Modifier.height(6.dp))
        lines.forEach { line ->
            Text(
                text = line.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp
            )
        }
    }

}