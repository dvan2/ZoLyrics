package com.dvan.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvan.zolyrics.data.model.SongSet
import kotlinx.coroutines.flow.Flow

@Dao
interface SongSetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSet(songSet: SongSet)

    @Query("SELECT * FROM song_sets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllSetsForUser(userId: String): Flow<List<SongSet>>

    @Query("DELETE FROM song_sets WHERE id = :setId")
    suspend fun deleteSetById(setId: String)

    @Query("SELECT title FROM song_sets WHERE id = :setId LIMIT 1")
    fun getSetTitle(setId: String): Flow<String?>
}