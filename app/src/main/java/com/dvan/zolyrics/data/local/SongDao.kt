package com.dvan.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dvan.zolyrics.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getAllSongs(): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: Song): Long

    @Update
    suspend fun updateSong(song: Song): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllSong(songs: List<Song>)

    @Transaction
    suspend fun upsertAll(songs: List<Song>) {
        for (song in songs) {
            val id = insertSong(song)
            if (id == -1L) {
                updateSong(song)
            }
        }
    }

    @Query("DELETE FROM song")
    suspend fun clearAll()

    @Query("SELECT * FROM song WHERE id = :id LIMIT 1")
    suspend fun getSongById(id: String): Song?
}