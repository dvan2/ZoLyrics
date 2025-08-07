package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "song_sets")
data class SongSet(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String
)
