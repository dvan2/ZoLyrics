package com.dvan.zolyrics.data.repositories

import android.util.Log
import com.dvan.zolyrics.data.local.FavoriteDao
import com.dvan.zolyrics.data.local.LyricDao
import com.dvan.zolyrics.data.local.SongDao
import com.dvan.zolyrics.data.model.FavoriteSong
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.remote.SupabaseService
import com.dvan.zolyrics.ui.screens.search.SearchResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
        try {
            val songs = supabaseService.getAllSongs()
            if (songs.isNotEmpty()) {
                songDao.upsertAll(songs)
            }
        } catch (t: Throwable) {
            Log.w("SongRepository", "Refresh failed, using local cache: ${t.message}")
        }
    }


    fun getLyrics(songId: String): Flow<List<LyricLine>> = lyricDao.getLyrics(songId)

    suspend fun loadLyricsFromSupabase(songId: String) {
        try {
            val remoteLyrics = supabaseService.getLyrics(songId)
            lyricDao.insertAll(remoteLyrics)
        } catch (t: Throwable) {
            Log.w("SongRepository", "Lyrics fetch failed")
        }
    }

    fun getFavorites(): Flow<List<FavoriteSong>> = favoriteDao.getAllFavorites()
    fun isFavorite(songId: String): Flow<Boolean> = favoriteDao.isFavorite(songId)
    suspend fun addFavorite(songId: String) = favoriteDao.insert(FavoriteSong(songId))
    suspend fun removeFavorite(songId: String) = favoriteDao.delete(songId)

    suspend fun searchSongsWithLyricMatches(query: String): List<SearchResult> {
        val titleHits = lyricDao.searchByTitleOrArtist(query)
        val lyricHits = lyricDao.searchLyricMatches(query)

        val titleResults = titleHits.map { song ->
            SearchResult(song = song, matchedLine = null)
        }

        val lyricGrouped = lyricHits.groupBy { it.songId }.mapNotNull { entry ->
            val songId = entry.key
            val lines = entry.value
            val song = songDao.getSongById(songId) ?: return@mapNotNull null
            SearchResult(song = song, matchedLine = lines.first().matchedLine)
        }

        return (titleResults + lyricGrouped).distinctBy { it.song.id }
    }

    suspend fun preloadLyricsIfNeeded() {
        try {
            val songs = songDao.getAllSongs().first()
            val lyricCount = lyricDao.count()

            if (lyricCount < songs.size * 3) { // Estimate: avg 3+ lines per song
                // Load in batches of 10 with 1 second delay between
                val batches = songs.chunked(10)
                for ((i, batch) in batches.withIndex()) {
                    batch.forEach { song ->
                        val localLyrics = lyricDao.getLyricsSync(song.id)
                        if (localLyrics.isEmpty()) {
                            loadLyricsFromSupabase(song.id)
                        }
                    }
                    Log.d("PreloadLyrics", "Batch ${i + 1} of ${batches.size} completed")
                    delay(1000) // Wait 1 second between batches to avoid Supabase rate limits
                }

                Log.d("PreloadLyrics", "Finished preloading lyrics.")
            } else {
                Log.d("PreloadLyrics", "Lyrics already cached. Skipping preload.")
            }
        } catch (e: Exception) {
            Log.e("SongRepository", "Preloading lyrics failed: ${e.message}")
        }
    }
}