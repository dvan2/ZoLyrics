package com.example.zolyrics.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LyricSection(
    val id: String,
    @SerialName("song_id")
    val songId: String,
    @SerialName("section_type")
    val sectionType: String,
    val lines: List<String>
)

@Serializable
data class SongWithLyrics(
    val id: String,
    val title: String,
    val artist: String,
    @SerialName("lyric_sections")
    val lyricSection: LyricSection
)

@Entity(tableName = "song")
@Serializable
data class Song(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String
)
