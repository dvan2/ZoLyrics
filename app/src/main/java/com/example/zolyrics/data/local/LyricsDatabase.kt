package com.example.zolyrics.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zolyrics.data.model.FavoriteSong
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.SetItem
import com.example.zolyrics.data.model.SetSongKeyOverride
import com.example.zolyrics.data.model.SetSongKeyOverrideDao
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongSet

@Database(entities = [Song::class, FavoriteSong::class, LyricLine::class, SongSet::class, SetItem::class,
    SetSongKeyOverride::class],
    version = 6, exportSchema = false)
abstract class LyricsDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun lyricDao(): LyricDao
    abstract fun songSetDao(): SongSetDao
    abstract fun setItemDao(): SetItemDao
    abstract fun setSongKeyOverrideDao(): SetSongKeyOverrideDao

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
