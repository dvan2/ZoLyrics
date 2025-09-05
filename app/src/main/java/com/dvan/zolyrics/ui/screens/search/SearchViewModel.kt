package com.dvan.zolyrics.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dvan.zolyrics.LyricsApplication
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.repositories.SongRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: SongRepository
): ViewModel() {

    var query by mutableStateOf("")
    var results by mutableStateOf<List<SearchResult>>(emptyList())


    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            results = if (query.isBlank()) emptyList()
            else repo.searchSongsWithLyricMatches(query)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LyricsApplication
                SearchViewModel(app.container.songRepository)
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
