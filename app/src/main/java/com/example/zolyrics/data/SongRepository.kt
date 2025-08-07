package com.example.zolyrics.data

import com.example.zolyrics.data.local.FavoriteDao
import com.example.zolyrics.data.local.LyricDao
import com.example.zolyrics.data.local.SongDao
import com.example.zolyrics.data.model.FavoriteSong
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow

class SongRepository(
    private val supabaseService: SupabaseService,
    private val songDao: SongDao,
    private val favoriteDao: FavoriteDao,
    private val lyricDao: LyricDao
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
        songDao.upsertAll(songs)
    }

    suspend fun getSongsWithLyrics(songId: String): List<LyricLine> {
        return supabaseService.getLyrics(songId)
    }

    fun getLyrics(songId: String): Flow<List<LyricLine>> = lyricDao.getLyrics(songId)

    suspend fun loadLyricsFromSupabase(songId: String) {
        val remoteLyrics = supabaseService.getLyrics(songId)
        lyricDao.insertAll(remoteLyrics)
    }

    suspend fun refreshAllLyrics() = lyricDao.clearAll()

    fun getFavorites(): Flow<List<FavoriteSong>> = favoriteDao.getAllFavorites()
    fun isFavorite(songId: String): Flow<Boolean> = favoriteDao.isFavorite(songId)
    suspend fun addFavorite(songId: String) = favoriteDao.insert(FavoriteSong(songId))
    suspend fun removeFavorite(songId: String) = favoriteDao.delete(songId)
}