package com.example.zolyrics.data

import android.content.Context
import com.example.zolyrics.data.local.LyricsDatabase
import com.example.zolyrics.data.local.SongSetDao
import com.example.zolyrics.data.remote.SupabaseService

interface AppContainer {
    val songRepository: SongRepository
    val userRepository: UserRepository
}

class AppDataContainer(context: Context) : AppContainer {
    private val database = LyricsDatabase.getDatabase(context)
    private val songDao = database.songDao()
    private val favoriteDao = database.favoriteDao()
    private val lyricDao = database.lyricDao()
    private val supabaseService = SupabaseService()

    private val songSetDao = database.songSetDao()
    private val setItemDao = database.setItemDao()

    override val songRepository: SongRepository by lazy {
        SongRepository(supabaseService, songDao, favoriteDao, lyricDao)
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(songSetDao, setItemDao, songDao)
    }

}