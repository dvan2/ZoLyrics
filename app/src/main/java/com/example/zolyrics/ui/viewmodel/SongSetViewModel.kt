package com.example.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zolyrics.LyricsApplication
import com.example.zolyrics.data.model.SetItem
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongSet
import com.example.zolyrics.data.repositories.KeyOverrideRepository
import com.example.zolyrics.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongSetViewModel(
    private val repository: UserRepository,
    private val keyOverrideRepository: KeyOverrideRepository
) : ViewModel() {

    val allSets: StateFlow<List<SongSet>> = repository.getAllSets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSetSongs = MutableStateFlow<List<Song>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentSetId = MutableStateFlow<String?>(null)

    private val _orderedSongs = MutableStateFlow<List<SongInSetUi>>(emptyList())
    val songsInSetUi: StateFlow<List<SongInSetUi>> = _orderedSongs

    fun persistSetOrder() {
        viewModelScope.launch {
            val setId = _currentSetId.value ?: return@launch
            val updated = _orderedSongs.value.mapIndexed { index, it ->
                SetItem(
                    setId = setId,
                    songId = it.song.id,
                    position = index
                )
            }
            repository.updateSetItemPositions(updated)
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val current = _orderedSongs.value.toMutableList()
        if (fromIndex in current.indices && toIndex in 0..current.size) {
            val item = current.removeAt(fromIndex)
            current.add(toIndex, item)
            _orderedSongs.value = current
        }
    }

    fun loadSongsForSet(setId: String) {
        viewModelScope.launch {
            _currentSetId.value = setId
            _isLoading.value = true

            repository.getSongsForSet(setId).collect { songs ->
                val overrides = keyOverrideRepository.getOverrides(setId)
                val overrideMap = overrides.associateBy { it.songId }

                val items = songs.map { s ->
                    val o = overrideMap[s.id]
                    SongInSetUi(
                        song = s,
                        originalKey = s.key.orEmpty(),
                        preferredKey = o?.preferredKey
                    )
                }

                _orderedSongs.value = items
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

    fun deleteSet(setId: String) = viewModelScope.launch {
        repository.deleteSetWithItems(setId)
    }

    fun setPreferredKeyForSong(songId: String, key: String) = viewModelScope.launch {
        val setId = _currentSetId.value ?: return@launch
        keyOverrideRepository.setPreferredKey(setId, songId, key)
    }

    fun clearPreferredKeyForSong(songId: String) = viewModelScope.launch {
        val setId = _currentSetId.value ?: return@launch
        keyOverrideRepository.clearPreferredKey(setId, songId)
    }


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