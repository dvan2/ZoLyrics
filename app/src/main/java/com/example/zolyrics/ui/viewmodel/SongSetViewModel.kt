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
import com.example.zolyrics.data.repositories.KeyOverrideRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongSetViewModel(
    private val repository: UserRepository,
    private val keyOverrideRepository: KeyOverrideRepository
) : ViewModel() {

    val allSets: StateFlow<List<SongSet>> = repository.getAllSets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSetSongs = MutableStateFlow<List<Song>>(emptyList())
    val currentSetSongs: StateFlow<List<Song>> = _currentSetSongs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentSetId = MutableStateFlow<String?>(null)

    fun loadSongsForSet(setId: String) {
        viewModelScope.launch {
            _currentSetId.value = setId
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

    fun setPreferredKeyForSong(songId: String, key: String) = viewModelScope.launch {
        val setId = _currentSetId.value ?: return@launch
        keyOverrideRepository.setPreferredKey(setId, songId, key)
    }

    fun clearPreferredKeyForSong(songId: String) = viewModelScope.launch {
        val setId = _currentSetId.value ?: return@launch
        keyOverrideRepository.clearPreferredKey(setId, songId)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val songsInSetUi: StateFlow<List<SongInSetUi>> =
        combine(currentSetSongs, _currentSetId.flatMapLatest { setId ->
            if (setId != null) keyOverrideRepository.observeOverrides(setId)
            else flowOf(emptyList())
        }) { songs, overrides ->
            val map = overrides.associateBy { it.songId }
            songs.map { s ->
                val o = map[s.id]
                SongInSetUi(
                    song = s,
                    originalKey = s.key.orEmpty(),
                    preferredKey = o?.preferredKey,
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LyricsApplication)
                val userRepo = application.container.userRepository
                val overrideRepo = application.container.overrideRepository
                SongSetViewModel(userRepo,overrideRepo)
            }
        }
    }

}

data class SongInSetUi(
    val song: Song,
    val originalKey: String,
    val preferredKey: String?,
)