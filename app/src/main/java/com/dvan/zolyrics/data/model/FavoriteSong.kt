package com.dvan.zolyrics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteSong(
    @PrimaryKey val songId: String
)