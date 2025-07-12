package com.example.zolyrics.data

import android.content.Context
import com.example.zolyrics.data.local.LyricsDatabase
import com.example.zolyrics.data.remote.SupabaseService

interface AppContainer {
    val songRepository: SongRepository
}

class AppDataContainer(context: Context) : AppContainer {
    private val database = LyricsDatabase.getDatabase(context)
    private val songDao = database.songDao()
    private val supabaseService = SupabaseService()

    override val songRepository: SongRepository by lazy {
        SongRepository(supabaseService, songDao)
    }
}