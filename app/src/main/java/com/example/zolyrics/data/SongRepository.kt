package com.example.zolyrics.data

import com.example.zolyrics.data.local.SongDao
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongWithLyrics
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

    suspend fun refreshSongsFromSupabase() {
        val songs = supabaseService.getAllSongs()
        songDao.clearAll()
        songDao.insertAllSong(songs)
    }

    suspend fun getSongsWithLyrics(songId: String): List<LyricLine> {
        return supabaseService.getLyrics(songId)
    }

}