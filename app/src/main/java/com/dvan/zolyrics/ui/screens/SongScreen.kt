package com.dvan.zolyrics.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.ui.screens.components.LyricsSectionDisplay
import com.dvan.zolyrics.ui.screens.components.SongHeaderWithPref
import com.dvan.zolyrics.ui.viewmodel.PreferredKeyViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongScreen(
    song: Song, lyrics: List<LyricLine>,
) {
    val SectionsStartIndex = 2


    val prefsVm: PreferredKeyViewModel = viewModel(factory = PreferredKeyViewModel.Factory)
    val prefMap by prefsVm.map.collectAsState()
    val globalPrefFortThisSong = prefMap[song.id]

    val sections = remember(lyrics) { buildSections(lyrics) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var chipsHeightPx by remember { mutableStateOf(0) }


    val currentIndex by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val pinTop = /* the y where content sits under chips */
                chipsHeightPx // viewportStartOffset is 0 here, so just use chipsHeight

            val firstVisibleSection = info.visibleItemsInfo
                .filter { it.index >= SectionsStartIndex }
                .minByOrNull { item -> abs(item.offset - pinTop) }

            val idx = firstVisibleSection?.index ?: SectionsStartIndex
            (idx - SectionsStartIndex).coerceIn(0, (sections.size - 1).coerceAtLeast(0))
        }
    }


    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 0. Header (scrolls away)
        item {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                SongHeaderWithPref(
                    title = song.title,
                    artist = song.artistName,
                    bpm = song.bpm,
                    originalKey = song.key ?: "N/A",
                    globalPreferredKey = globalPrefFortThisSong,
                    onSetGlobalPreferredKey = { newKey -> prefsVm.setGlobalPreferredKey(song.id, newKey) },
                    onClearGlobalPreferredKey = { prefsVm.clearGlobalPreferredKey(song.id) }
                )
                Spacer(Modifier.height(8.dp))
            }

        }

        stickyHeader("chips") {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                shadowElevation = 3.dp,
                modifier = Modifier.onGloballyPositioned { chipsHeightPx = it.size.height } // <—
            ) {
                JumpToSectionChips(
                    titles = sections.map { it.title },
                    selectedIndex = currentIndex,
                    onJump = { idx ->
                        scope.launch {
                            listState.animateScrollToItem(SectionsStartIndex +  idx, -chipsHeightPx)
                        }
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }


        // 2. Sections
        itemsIndexed(sections, key = { i, s -> "${i}-${s.title}" }) { _, section ->
            LyricsSectionDisplay(section.title, section.lines)
            Spacer(Modifier.height(12.dp))
        }
    }
}

// Represents one lyrics block on screen
data class SectionBlock(
    val title: String,
    val lines: List<LyricLine>
)

/** Build ordered sections from your flat LyricLine list */
fun buildSections(lyrics: List<LyricLine>): List<SectionBlock> {
    if (lyrics.isEmpty()) return emptyList()

    // preserve first-seen order of sections
    val order = LinkedHashMap<String, MutableList<LyricLine>>()
    lyrics.forEach { line ->
        order.getOrPut(line.sectionType) { mutableListOf() }.add(line)
    }

    return order.map { (title, items) ->
        SectionBlock(
            title = title,
            lines = items.sortedBy { it.lineNumber } // safe even if nulls
        )
    }
}

@Composable
private fun JumpToSectionChips(
    titles: List<String>,
    selectedIndex: Int,
    onJump: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(titles) { i, raw ->
            FilterChip(
                selected = i == selectedIndex,
                onClick = { onJump(i) },
                label = { Text(raw.uppercase()) }
            )
        }
    }
}