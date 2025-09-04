package com.dvan.zolyrics.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dvan.zolyrics.data.model.LyricLine

@Composable
fun LyricsSectionDisplay(
    sectionTitle: String,
    lines: List<LyricLine>
) {
    val isChorus = sectionTitle.contains("chorus", true)
    val isBridge = sectionTitle.contains("bridge", true)
    val isPre = sectionTitle.contains("pre-chorus", true)

    val hasBlock = isChorus || isBridge || isPre
    val container = when {
        isChorus -> MaterialTheme.colorScheme.surfaceContainerHigh
        isBridge || isPre -> MaterialTheme.colorScheme.surfaceContainer
        else -> null
    }
    val accent = when {
        isChorus -> MaterialTheme.colorScheme.primary.copy(alpha = 0.24f)
        isBridge || isPre -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
        else -> null
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (hasBlock) {
                    Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(container!!)
                        .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                        .drawBehind {
                            accent?.let {
                                drawRect(
                                    color = it,
                                    topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                                )
                            }
                        }
                        .padding(start = 10.dp) // offset content away from the bar
                } else Modifier
            )
    ) {
        SectionHeaderPill(sectionTitle)
        Spacer(Modifier.height(8.dp))
        lines.forEach { line ->
            Text(
                line.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp
                )
            )
        }
    }
}

@Composable
fun SectionHeaderPill(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            letterSpacing = 0.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}