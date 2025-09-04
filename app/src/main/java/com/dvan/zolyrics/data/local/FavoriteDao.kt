package com.dvan.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dvan.zolyrics.data.model.FavoriteSong
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteSong>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE songId = :songId)")
    fun isFavorite(songId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteSong)

    @Query("DELETE FROM favorites WHERE songId = :songId")
    suspend fun delete(songId: String)
}