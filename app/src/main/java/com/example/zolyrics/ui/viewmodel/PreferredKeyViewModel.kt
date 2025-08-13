package com.example.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zolyrics.LyricsApplication
import com.example.zolyrics.data.repositories.UserKeyPrefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferredKeyViewModel(
    private val repo: UserKeyPrefRepository
): ViewModel() {
    private val _map = MutableStateFlow<Map<String,String>>(emptyMap())
    val map: StateFlow<Map<String,String>> = _map

    init {
        viewModelScope.launch {
            repo.observeGlobalPrefs().collect { list ->
                _map.value = list.associate { it.songId to it.preferredKey }
            }
        }
    }

    fun setGlobalPreferredKey(songId: String, key: String) = viewModelScope.launch {
        repo.setGlobalPreferredKey(songId, key)
    }

    fun clearGlobalPreferredKey(songId: String) = viewModelScope.launch {
        repo.clearGlobalPreferredKey(songId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LyricsApplication)
                PreferredKeyViewModel(app.container.preferredKeyRepository)
            }
        }
    }

}