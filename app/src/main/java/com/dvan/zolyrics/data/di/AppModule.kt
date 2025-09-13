package com.dvan.zolyrics.data.di

import com.dvan.zolyrics.data.remote.SupabaseService
import com.dvan.zolyrics.data.repositories.KeyOverrideRepository
import com.dvan.zolyrics.data.repositories.SongRepository
import com.dvan.zolyrics.data.repositories.UserKeyPrefRepository
import com.dvan.zolyrics.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseService(): SupabaseService {
        return SupabaseService()
    }

    @Provides
    @Singleton
    fun provideSongRepository(
        supabaseService: SupabaseService,
        songDao: com.dvan.zolyrics.data.local.SongDao,
        favoriteDao: com.dvan.zolyrics.data.local.FavoriteDao,
        lyricDao: com.dvan.zolyrics.data.local.LyricDao
    ): SongRepository {
        return SongRepository(supabaseService, songDao, favoriteDao, lyricDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        songSetDao: com.dvan.zolyrics.data.local.SongSetDao,
        setItemDao: com.dvan.zolyrics.data.local.SetItemDao,
        songDao: com.dvan.zolyrics.data.local.SongDao
    ): UserRepository {
        return UserRepository(songSetDao, setItemDao, songDao)
    }

    @Provides
    @Singleton
    fun provideKeyOverrideRepository(dao: com.dvan.zolyrics.data.model.SetSongKeyOverrideDao): KeyOverrideRepository {
        return KeyOverrideRepository(dao)
    }

    @Provides
    @Singleton
    fun provideUserKeyPrefRepository(dao: com.dvan.zolyrics.data.local.UserSongKeyPrefDao): UserKeyPrefRepository {
        return UserKeyPrefRepository(dao)
    }
}
