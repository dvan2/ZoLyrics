package com.example.zolyrics.data

import com.example.zolyrics.data.local.SongDao
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow

class SongRepository(
    private val supbaseService: SupabaseService,
    private val songDao: SongDao
) {
    fun getLocalSongs(): Flow<List<Song>> {
        return songDao.getAllSongs()
    }

}