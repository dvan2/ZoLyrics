package com.dvan.zolyrics.data.di

import android.content.Context
import com.dvan.zolyrics.data.local.FavoriteDao
import com.dvan.zolyrics.data.local.LyricDao
import com.dvan.zolyrics.data.local.LyricsDatabase
import com.dvan.zolyrics.data.local.SetItemDao
import com.dvan.zolyrics.data.local.SongDao
import com.dvan.zolyrics.data.local.SongSetDao
import com.dvan.zolyrics.data.local.UserSongKeyPrefDao
import com.dvan.zolyrics.data.model.SetSongKeyOverrideDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Dependencies will live as long as the application
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLyricsDatabase(@ApplicationContext context: Context): LyricsDatabase {
        return LyricsDatabase.getDatabase(context)
    }

    @Provides
    fun provideSongDao(database: LyricsDatabase): SongDao = database.songDao()

    @Provides
    fun provideFavoriteDao(database: LyricsDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideLyricDao(database: LyricsDatabase): LyricDao = database.lyricDao()

    @Provides
    fun provideSongSetDao(database: LyricsDatabase): SongSetDao = database.songSetDao()

    @Provides
    fun provideSetItemDao(database: LyricsDatabase): SetItemDao = database.setItemDao()

    @Provides
    fun provideSetSongKeyOverrideDao(database: LyricsDatabase): SetSongKeyOverrideDao = database.setSongKeyOverrideDao()

    @Provides
    fun provideUserSongKeyPrefDao(database: LyricsDatabase): UserSongKeyPrefDao = database.setUserSongKeyPrefDao()
}
