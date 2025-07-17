package com.example.zolyrics.data

import com.example.zolyrics.data.local.SongDao
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow

class SongRepository(
    private val supabaseService: SupabaseService,
    private val songDao: SongDao
) {
    fun getLocalSongs(): Flow<List<Song>> {
        return songDao.getAllSongs()
    }

    suspend fun syncSongsFromRemote() {
        val remoteSongs = supabaseService.getAllSongs()
        songDao.insertAllSong(remoteSongs)
    }

}