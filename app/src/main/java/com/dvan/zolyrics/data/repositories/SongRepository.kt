package com.dvan.zolyrics.data.repositories

import com.dvan.zolyrics.data.local.FavoriteDao
import com.dvan.zolyrics.data.local.LyricDao
import com.dvan.zolyrics.data.local.SongDao
import com.dvan.zolyrics.data.model.FavoriteSong
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.remote.SupabaseService
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

    suspend fun refreshSongsFromSupabase() {
        val songs = supabaseService.getAllSongs()
        songDao.upsertAll(songs)
    }


    fun getLyrics(songId: String): Flow<List<LyricLine>> = lyricDao.getLyrics(songId)

    suspend fun loadLyricsFromSupabase(songId: String) {
        val remoteLyrics = supabaseService.getLyrics(songId)
        lyricDao.insertAll(remoteLyrics)
    }

    fun getFavorites(): Flow<List<FavoriteSong>> = favoriteDao.getAllFavorites()
    fun isFavorite(songId: String): Flow<Boolean> = favoriteDao.isFavorite(songId)
    suspend fun addFavorite(songId: String) = favoriteDao.insert(FavoriteSong(songId))
    suspend fun removeFavorite(songId: String) = favoriteDao.delete(songId)
}