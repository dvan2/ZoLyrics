package com.example.zolyrics.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.screens.components.LyricsSectionDisplay
import com.example.zolyrics.ui.screens.components.SongHeader

@Composable
fun SongScreen(song: Song, lyrics: List<LyricLine>) {
    val groupedLyrics = lyrics
        .filter { it.songId == song.id }
        .sortedBy { it.lineNumber }
        .groupBy { it.sectionType }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        SongHeader(song.title, song.artist, bpm = 100, key = "C Major") // Replace bpm/key later

        Spacer(Modifier.height(24.dp))

        // Show each section
        groupedLyrics.forEach { (section, lines) ->
            LyricsSectionDisplay(section, lines)
        }
    }
}