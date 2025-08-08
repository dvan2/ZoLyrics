package com.example.zolyrics.data

import android.content.Context
import com.example.zolyrics.data.local.LyricsDatabase
import com.example.zolyrics.data.remote.SupabaseService
import com.example.zolyrics.data.repositories.KeyOverrideRepository

interface AppContainer {
    val songRepository: SongRepository
    val userRepository: UserRepository
    val overrideRepository: KeyOverrideRepository
}

class AppDataContainer(context: Context) : AppContainer {
    private val database = LyricsDatabase.getDatabase(context)
    private val songDao = database.songDao()
    private val favoriteDao = database.favoriteDao()
    private val lyricDao = database.lyricDao()
    private val supabaseService = SupabaseService()

    private val songSetDao = database.songSetDao()
    private val setItemDao = database.setItemDao()
    private val overrideDao = database.setSongKeyOverrideDao()

    override val songRepository: SongRepository by lazy {
        SongRepository(supabaseService, songDao, favoriteDao, lyricDao)
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(songSetDao, setItemDao, songDao)
    }

    override val overrideRepository: KeyOverrideRepository by lazy {
        KeyOverrideRepository(overrideDao)
    }

}