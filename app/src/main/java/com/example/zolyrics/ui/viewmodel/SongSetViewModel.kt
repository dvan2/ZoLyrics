package com.example.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zolyrics.LyricsApplication
import com.example.zolyrics.data.UserRepository
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongSetViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val allSets: StateFlow<List<SongSet>> = repository.getAllSets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSetSongs = MutableStateFlow<List<Song>>(emptyList())
    val currentSetSongs: StateFlow<List<Song>> = _currentSetSongs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadSongsForSet(setId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getSongsForSet(setId).collect { songs ->
                _currentSetSongs.value = songs
                _isLoading.value = false
            }
        }
    }

    fun createSet(
        title: String,
        songs: List<Song>,
        onDone: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.createSet(title, songs)
            onDone?.invoke()
        }
    }

    fun addSongToSet(setId: String, songId: String) {
        viewModelScope.launch {
            repository.addItemToSet(setId, songId)
            loadSongsForSet(setId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LyricsApplication)
                val userRepo = application.container.userRepository
                SongSetViewModel(userRepo)
            }
        }
    }

}