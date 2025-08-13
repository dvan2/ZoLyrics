package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_song_key_prefs",
    primaryKeys = ["userId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("songId")]
)
data class UserSongKeyPref(
    val userId: String,
    val songId: String,
    val preferredKey: String,
    val updatedAt: Long = System.currentTimeMillis()
)