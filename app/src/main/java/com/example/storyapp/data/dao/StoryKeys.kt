package com.example.storyapp.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_keys")
data class StoryKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
