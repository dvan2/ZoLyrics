package com.dvan.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.dvan.zolyrics.data.model.UserSongKeyPref
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSongKeyPrefDao {
    @Query("SELECT * FROM user_song_key_prefs WHERE userId = :userId AND songId = :songId LIMIT 1")
    suspend fun get(userId: String, songId: String): UserSongKeyPref?

    @Query("SELECT * FROM user_song_key_prefs WHERE userId = :userId")
    fun observeAll(userId: String): Flow<List<UserSongKeyPref>>

    @Upsert
    suspend fun upsert(pref: UserSongKeyPref)

    @Query("DELETE FROM user_song_key_prefs WHERE userId = :userId AND songId = :songId")
    suspend fun delete(userId: String, songId: String)
}