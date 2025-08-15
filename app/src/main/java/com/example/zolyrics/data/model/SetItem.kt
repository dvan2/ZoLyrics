package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "set_items",
    primaryKeys = ["setId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = SongSet::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("setId"),
        Index("songId")
    ]
)
data class SetItem (
    val setId: String,
    val songId: String,
    val position: Int
)