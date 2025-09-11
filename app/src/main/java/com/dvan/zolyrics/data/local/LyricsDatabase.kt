package com.dvan.zolyrics.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dvan.zolyrics.data.model.FavoriteSong
import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.SetItem
import com.dvan.zolyrics.data.model.SetSongKeyOverride
import com.dvan.zolyrics.data.model.SetSongKeyOverrideDao
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.model.SongSet
import com.dvan.zolyrics.data.model.UserSongKeyPref

@Database(entities = [Song::class, FavoriteSong::class, LyricLine::class, SongSet::class, SetItem::class,
    SetSongKeyOverride::class, UserSongKeyPref::class, LyricLineFts::class],
    version = 9, exportSchema = false)
abstract class LyricsDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun lyricDao(): LyricDao
    abstract fun songSetDao(): SongSetDao
    abstract fun setItemDao(): SetItemDao
    abstract fun setSongKeyOverrideDao(): SetSongKeyOverrideDao
    abstract fun setUserSongKeyPrefDao(): UserSongKeyPrefDao

    companion object {
        @Volatile
        private var INSTANCE: LyricsDatabase? = null

        fun getDatabase(context: Context): LyricsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LyricsDatabase::class.java,
                    "lyrics_database"
                )
                    .addCallback(object: RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("PRAGMA foreign_keys=ON;")
                        }
                    })
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
