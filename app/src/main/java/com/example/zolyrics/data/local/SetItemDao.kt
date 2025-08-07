package com.example.zolyrics.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zolyrics.data.model.SetItem

@Dao
interface SetItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSetItem(item: SetItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSetItem(items: List<SetItem>)

    @Query("SELECT * FROM set_items WHERE setId = :setId ORDER BY position")
    suspend fun getItemsForSetBlocking(setId: String): List<SetItem>

    @Delete
    suspend fun deleteSetItem(item: SetItem)
}