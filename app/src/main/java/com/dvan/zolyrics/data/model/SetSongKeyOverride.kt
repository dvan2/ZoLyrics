package com.dvan.zolyrics.data.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "set_song_key_overrides",
    primaryKeys = ["setId", "songId"]
)
data class SetSongKeyOverride(
    val setId: String,
    val songId: String,
    val preferredKey: String,
)

@Dao
interface SetSongKeyOverrideDao {
    @Query("SELECT * FROM set_song_key_overrides WHERE setId = :setId")
    fun observeOverrides(setId: String): Flow<List<SetSongKeyOverride>>

    @Query("SELECT * FROM set_song_key_overrides WHERE setId = :setId AND songId = :songId")
    suspend fun getOverride(setId: String, songId: String): SetSongKeyOverride?

    @Upsert
    suspend fun upsert(override: SetSongKeyOverride)

    @Query("DELETE FROM set_song_key_overrides WHERE setId = :setId AND songId = :songId")
    suspend fun clear(setId: String, songId: String)

    @Query("SELECT * FROM set_song_key_overrides WHERE setId = :setId")
    suspend fun getOverrides(setId: String): List<SetSongKeyOverride>
}