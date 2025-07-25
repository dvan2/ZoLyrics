package com.example.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zolyrics.data.model.LyricLine
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
}