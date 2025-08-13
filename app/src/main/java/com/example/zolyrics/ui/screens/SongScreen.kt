package com.example.zolyrics.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.ui.screens.components.LyricsSectionDisplay
import com.example.zolyrics.ui.screens.components.SongHeaderWithPref
import com.example.zolyrics.ui.viewmodel.PreferredKeyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongScreen(
    song: Song, lyrics: List<LyricLine>,
) {
    val prefsVm: PreferredKeyViewModel = viewModel(factory = PreferredKeyViewModel.Factory)
    val prefMap by prefsVm.map.collectAsState()
    val globalPrefFortThisSong = prefMap[song.id]

    val groupedLyrics = lyrics
        .filter { it.songId == song.id }
        .sortedBy { it.lineNumber }
        .groupBy { it.sectionType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    )
    {
        SongHeaderWithPref(
            title = song.title,
            artist = song.artist,
            bpm = song.bpm,
            originalKey = song.key ?: "N/A",
            globalPreferredKey = globalPrefFortThisSong,
            onSetGlobalPreferredKey = { newKey -> prefsVm.setGlobalPreferredKey(song.id, newKey) },
            onClearGlobalPreferredKey = { prefsVm.clearGlobalPreferredKey(song.id) }
        )

        Spacer(Modifier.height(24.dp))

        groupedLyrics.forEach { (section, lines) ->
            LyricsSectionDisplay(section, lines)
        }
    }
}