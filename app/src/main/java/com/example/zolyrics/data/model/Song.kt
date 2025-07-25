package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "lyric_lines")
@Serializable
data class LyricLine(
    @PrimaryKey
    val id: String,
    @SerialName("song_id")
    val songId: String,
    @SerialName("section_type")
    val sectionType: String,
    @SerialName("line_number")
    val lineNumber: Int,
    val content: String
)

@Entity(tableName = "song")
@Serializable
data class Song(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String
)
