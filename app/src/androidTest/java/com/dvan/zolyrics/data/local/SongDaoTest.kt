package com.dvan.zolyrics.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dvan.zolyrics.data.model.Song
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SongDaoTest {
    private lateinit var songDao: SongDao
    private lateinit var db: LyricsDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, LyricsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        songDao = db.songDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetSong() = runBlocking {
        val song = Song(
            id = "test-song-123",
            title = "Test Title",
            artistName = "Test Artist",
            bpm = 120,
            key = "C"
        )
        songDao.insertSong(song)
        val retrievedSong = songDao.getSongById(song.id)
        assertNotNull(retrievedSong)
        assertEquals(song.id, retrievedSong?.id)
        assertEquals(song.title, retrievedSong?.title)
        assertEquals(song.artistName, retrievedSong?.artistName)
    }
}
