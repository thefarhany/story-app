package com.example.storyapp.utils

import android.content.Context
import com.example.storyapp.data.UserPreferences
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.dao.StoryDatabase
import com.example.storyapp.data.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(pref, apiService, storyDatabase)
    }
}