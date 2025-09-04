package com.dvan.zolyrics.ui.model

import com.dvan.zolyrics.data.model.Song

interface SongInSetContract {
    val songId: String
    val song: Song
    val originalKey: String?
    val preferredKey: String?
}