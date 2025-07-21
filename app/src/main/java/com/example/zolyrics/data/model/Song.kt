package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LyricLine(
    val id: String,
    @SerialName("song_id")
    val songId: String,
    @SerialName("section_type")
    val sectionType: String,
    @SerialName("line_number")
    val lineNumber: Int,
    val content: String
)

@Serializable
data class SongWithLyrics(
    val id: String,
    val title: String,
    val artist: String,
    @SerialName("lyric_lines")
    val lyricSection: List<LyricLine>
)

@Entity(tableName = "song")
@Serializable
data class Song(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String
)
