package com.example.storyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<StoryKeys>)

    @Query("SELECT * FROM story_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): StoryKeys?

    @Query("DELETE FROM story_keys")
    suspend fun deleteRemoteKeys()
}