package com.example.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Query
import com.example.zolyrics.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getAllSongs(): Flow<List<Song>>

}