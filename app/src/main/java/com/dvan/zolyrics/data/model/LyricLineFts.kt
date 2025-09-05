// data/local/LyricLineFts.kt
package com.dvan.zolyrics.data.local

import androidx.room.Entity
import androidx.room.Fts4
import com.dvan.zolyrics.data.model.LyricLine

@Fts4(contentEntity = LyricLine::class)
@Entity(tableName = "lyric_lines_fts")
data class LyricLineFts(
    val content: String,
)

