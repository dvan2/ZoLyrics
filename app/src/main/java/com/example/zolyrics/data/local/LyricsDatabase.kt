package com.example.zolyrics.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zolyrics.data.model.FavoriteSong
import com.example.zolyrics.data.model.LyricLine
import com.example.zolyrics.data.model.Song

@Database(entities = [Song::class, FavoriteSong::class, LyricLine::class], version = 3, exportSchema = false)
abstract class LyricsDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun lyricDao(): LyricDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
