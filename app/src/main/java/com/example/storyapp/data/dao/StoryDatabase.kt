package com.example.storyapp.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.response.DetailStoryItem

@Database(
    entities = [DetailStoryItem::class, StoryKeys::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun storyKeysDao(): StoryKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}