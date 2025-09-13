package com.dvan.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvan.zolyrics.data.model.FavoriteSong
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LyricsUiState {
    object Loading : LyricsUiState()
    data class Success(val lyrics: List<LyricLine>) : LyricsUiState()
    data class Error(val message: String) : LyricsUiState()
}

@HiltViewModel
class SongViewModel @Inject constructor(private val repository: SongRepository): ViewModel(){
    val localSongs: Flow<List<Song>> = repository.getLocalSongs()

    private val _lyricsState = MutableStateFlow<LyricsUiState>(LyricsUiState.Loading)
    val lyricsState: StateFlow<LyricsUiState> = _lyricsState

    val favorites: Flow<List<FavoriteSong>> = repository.getFavorites()

    private var lastLoadedSongId: String? = null

    init {
        viewModelScope.launch {
            repository.refreshSongsFromSupabase()
            repository.preloadLyricsIfNeeded()
        }
    }

    fun loadLyrics(songId: String) {
        viewModelScope.launch {
            _lyricsState.value = LyricsUiState.Loading

            try{
                val local = repository.getLyrics(songId).first()

                if (local.isNotEmpty()) {
                    _lyricsState.value = LyricsUiState.Success(local)
                } else {
                    repository.loadLyricsFromSupabase(songId)
                    val updated = repository.getLyrics(songId).first()
                    _lyricsState.value = LyricsUiState.Success(updated)
                }

                lastLoadedSongId = songId
            }catch (e: Exception) {
                _lyricsState.value = LyricsUiState.Error("Failed to load lyrics: ${e.message}")
            }

        }
    }

    fun isFavorite(songId: String): Flow<Boolean> {
        return repository.isFavorite(songId)
    }

    fun toggleFavorite(songId: String) {
        viewModelScope.launch {
            val isFav = repository.isFavorite(songId).first()
            repository.getFavorites().first()
                if (isFav) {
                    repository.removeFavorite(songId)
                } else {
                    repository.addFavorite(songId)
                }
        }
    }
}