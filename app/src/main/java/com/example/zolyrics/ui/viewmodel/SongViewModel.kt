package com.example.zolyrics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.zolyrics.LyricsApplication
import com.example.zolyrics.data.SongRepository
import com.example.zolyrics.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SongViewModel (private val repository: SongRepository): ViewModel(){
    val localSongs: Flow<List<Song>> = repository.getLocalSongs()

    init {
        viewModelScope.launch {
            repository.syncSongsFromRemote()
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