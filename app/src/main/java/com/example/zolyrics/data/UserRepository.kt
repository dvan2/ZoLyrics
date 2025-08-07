package com.example.zolyrics.data

import com.example.zolyrics.data.local.SetItemDao
import com.example.zolyrics.data.local.SongDao
import com.example.zolyrics.data.local.SongSetDao
import com.example.zolyrics.data.model.SetItem
import com.example.zolyrics.data.model.Song
import com.example.zolyrics.data.model.SongSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.UUID

class UserRepository(
    private val songSetDao: SongSetDao,
    private val setItemDao: SetItemDao,
    private val songDao: SongDao,
    private val userId: String = LOCAL_USER_ID,
) {
    suspend fun createSet(title: String, songs: List<Song>) {
        try {
            val setId = UUID.randomUUID().toString()
            val set = SongSet(id = setId, title = title, userId = userId)
            songSetDao.insertSet(set)

            songs.forEachIndexed { index, song ->
                val item = SetItem(setId = setId, songId = song.id, position = index)

                setItemDao.insertSetItem(item)
            }
        } catch (e: Exception) {
            println("❌ Error inserting set or items: ${e.localizedMessage}")
        }
    }

    suspend fun addItemToSet(setId: String, songId: String) {
        // figure out next position (e.g. current count)
        val nextPos = setItemDao.getItemsForSetBlocking(setId).size
        val item = SetItem(setId = setId, songId = songId, position = nextPos)
        setItemDao.insertSetItem(item)
    }

    fun getAllSets(): Flow<List<SongSet>> =
        songSetDao.getAllSetsForUser(userId)

    fun getSongsForSet(setId: String): Flow<List<Song>> = flow {
        val setItems = setItemDao.getItemsForSetBlocking(setId)
        val songs = setItems.mapNotNull{ item ->
            songDao.getSongById(item.songId)
        }
        emit(songs)
    }

    companion object {
        const val LOCAL_USER_ID = "local-default-user"
    }
}