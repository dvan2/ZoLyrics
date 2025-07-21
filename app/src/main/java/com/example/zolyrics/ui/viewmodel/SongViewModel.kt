package com.example.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zolyrics.LyricsApplication
import com.example.zolyrics.data.SongRepository
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongViewModel (private val repository: SongRepository): ViewModel(){
    val localSongs: Flow<List<Song>> = repository.getLocalSongs()
    private val _selectedLyrics = MutableStateFlow<List<LyricLine>>(emptyList())
    val selectedLyrics: StateFlow<List<LyricLine>> = _selectedLyrics

    init {
        viewModelScope.launch {
            repository.refreshSongsFromSupabase()
        }
    }

    fun loadLyrics(songId: String) {
        viewModelScope.launch {
            _selectedLyrics.value = repository.getSongsWithLyrics(songId)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LyricsApplication)
                val songRepository = application.container.songRepository
                SongViewModel(songRepository)
            }
        }
    }

}