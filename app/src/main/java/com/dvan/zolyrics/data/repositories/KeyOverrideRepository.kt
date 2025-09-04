package com.dvan.zolyrics.data.repositories

import com.dvan.zolyrics.data.model.SetSongKeyOverride
import com.dvan.zolyrics.data.model.SetSongKeyOverrideDao


class KeyOverrideRepository(
    private val dao: SetSongKeyOverrideDao
) {
    fun observeOverrides(setId: String) = dao.observeOverrides(setId)

    suspend fun getOverride(setId: String, songId: String) = dao.getOverride(setId, songId)

    suspend fun getOverrides(setId: String): List<SetSongKeyOverride> {
        return dao.getOverrides(setId)
    }

    suspend fun setPreferredKey(setId: String, songId: String, key: String) =
        dao.upsert(SetSongKeyOverride(setId, songId, key))

    suspend fun clearPreferredKey(setId: String, songId: String) = dao.clear(setId, songId)
}
