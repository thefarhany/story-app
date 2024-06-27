package com.example.storyapp.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.response.DetailStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<DetailStoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, DetailStoryItem>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()
}