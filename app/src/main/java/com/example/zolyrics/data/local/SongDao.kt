package com.example.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zolyrics.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getAllSongs(): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllSong(songs: List<Song>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(songs: List<Song>)

    @Query("DELETE FROM song")
    suspend fun clearAll()

    @Query("SELECT * FROM song WHERE id = :id LIMIT 1")
    suspend fun getSongById(id: String): Song?
}