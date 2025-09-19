package com.dvan.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvan.zolyrics.data.model.SetItem
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.model.SongSet
import com.dvan.zolyrics.data.repositories.KeyOverrideRepository
import com.dvan.zolyrics.data.repositories.UserRepository
import com.dvan.zolyrics.ui.model.SongInSetContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongSetViewModel @Inject constructor(
    private val repository: UserRepository,
    private val keyOverrideRepository: KeyOverrideRepository
) : ViewModel() {

    val allSets: StateFlow<List<SongSet>> = repository.getAllSets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentSetId = MutableStateFlow<String?>(null)

    private val _orderedSongs = MutableStateFlow<List<SongInSetUi>>(emptyList())
    val songsInSetUi: StateFlow<List<SongInSetUi>> = _orderedSongs


    private val _setTitle = MutableStateFlow<String?>(null)
    val setTitle: StateFlow<String?> = _setTitle

    fun loadSetTitle(setId: String) {
        viewModelScope.launch {
            repository.getSetTitle(setId).collect { title ->
                _setTitle.value = title
            }
        }
    }

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

        //ensure valid index
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

        _orderedSongs.value = _orderedSongs.value.map { ui ->
            if (ui.song.id == songId) ui.copy(preferredKey = key) else ui
        }
    }

    fun clearPreferredKeyForSong(songId: String) = viewModelScope.launch {
        val setId = _currentSetId.value ?: return@launch
        keyOverrideRepository.clearPreferredKey(setId, songId)

        _orderedSongs.value = _orderedSongs.value.map { ui ->
            if (ui.song.id == songId) ui.copy(preferredKey = null) else ui
        }
    }

    fun addSongToSet(setId: String, songId: String) = viewModelScope.launch {
        repository.addSongToSet(setId, songId)
    }

    suspend fun isSongInSet(setId: String, songId: String): Boolean {
        return repository.isSongInSet(setId, songId)
    }

    fun removeSongFromSet(setId: String, songId: String) = viewModelScope.launch {
        repository.removeSongFromSet(setId, songId)

        // update local state so UI refreshes without waiting for re-collect
        _orderedSongs.value = _orderedSongs.value.filterNot { it.song.id == songId }
    }

}

data class SongInSetUi(
    override val song: Song,
    override val originalKey: String?,
    override val preferredKey: String?,
): SongInSetContract {
    override val songId: String get() = song.id
}