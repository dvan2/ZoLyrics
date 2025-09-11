package com.dvan.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.ui.screens.search.MatchedLyric
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricDao {
    @Query("SELECT * FROM lyric_lines WHERE songId = :songId ORDER BY lineNumber")
    fun getLyrics(songId: String): Flow<List<LyricLine>>

    @Query("SELECT * FROM lyric_lines WHERE songId = :songId ORDER BY lineNumber")
    suspend fun getLyricsSync(songId: String): List<LyricLine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lyrics: List<LyricLine>)

    @Query("DELETE FROM lyric_lines")
    suspend fun clearAll()

    @Query("""
    SELECT s.* FROM song s
    JOIN lyric_lines l ON s.id = l.songId
    JOIN lyric_lines_fts fts ON fts.content = l.content
    WHERE fts.content MATCH :query
""")
    suspend fun searchSongsByLyrics(query: String): List<Song>

    @Query("""
    SELECT * FROM song
    WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' 
       OR LOWER(artistName) LIKE '%' || LOWER(:query) || '%'
""")
    suspend fun searchByTitleOrArtist(query: String): List<Song>

    @Query("""
    SELECT l.songId AS songId, l.content AS matchedLine 
    FROM lyric_lines l
    JOIN lyric_lines_fts fts ON fts.content = l.content
    WHERE fts.content MATCH :query
""")
    suspend fun searchLyricMatches(query: String): List<MatchedLyric>

    @Query("SELECT COUNT(*) FROM lyric_lines")
    suspend fun count(): Int
}