package com.dvan.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvan.zolyrics.data.repositories.UserKeyPrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferredKeyViewModel @Inject constructor(
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
}