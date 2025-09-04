package com.dvan.zolyrics.data.repositories

import com.dvan.zolyrics.data.local.UserSongKeyPrefDao
import com.dvan.zolyrics.data.model.UserSongKeyPref
import kotlinx.coroutines.flow.Flow

class UserKeyPrefRepository(
    private val dao: UserSongKeyPrefDao,
    private val userId: String = UserRepository.LOCAL_USER_ID
) {
    suspend fun setGlobalPreferredKey(songId: String, key: String) {
        dao.upsert(UserSongKeyPref(userId = userId, songId = songId, preferredKey = key))
    }

    suspend fun clearGlobalPreferredKey(songId: String) {
        dao.delete(userId, songId)
    }

    suspend fun getGlobalPreferredKey(songId: String): String? =
        dao.get(userId, songId)?.preferredKey

    fun observeGlobalPrefs(): Flow<List<UserSongKeyPref>> =
        dao.observeAll(userId)
}