package com.example.zolyrics.ui.model

import com.example.zolyrics.data.model.Song

interface SongInSetContract {
    val songId: String
    val song: Song
    val originalKey: String?
    val preferredKey: String?
}