package com.dvan.zolyrics.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SongRepository
): ViewModel() {

    var query by mutableStateOf("")
        private set

    var results by mutableStateOf<List<SearchResult>>(emptyList())
        private set

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            results = if (query.isBlank()) {
                emptyList()
            } else {
                val ftsQuery = query.trim()
                    .split("\\s+".toRegex())
                    .joinToString(" ") { "$it*" }

                repo.searchSongsWithLyricMatches(ftsQuery)
            }
        }
    }
}

data class SearchResult(
    val song: Song,
    val matchedLine: String?
)

data class MatchedLyric(
    val songId: String,
    val matchedLine: String
)
