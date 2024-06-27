package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.R
import com.example.storyapp.data.dao.StoryDatabase
import com.example.storyapp.data.response.DetailStoryItem
import com.example.storyapp.data.response.FileUploadResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.data.response.UserModel
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.ui.paging.StoryRemoteMediator
import com.example.storyapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreferences,
    private var apiService: ApiService,
    private val storyDatabase: StoryDatabase,
){
    suspend fun saveUser(user: UserModel) {
        userPreference.logout()
        userPreference.saveUser(user)
    }

    fun getUser(): Flow<UserModel> {
        return userPreference.getUser()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val res = apiService.register(name, email, password)
            Result.Success(res)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.error_occurred.toString())
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        wrapEspressoIdlingResource {
            return try {
                val res = apiService.login(email, password)
                Result.Success(res)
            } catch (e: Exception) {
                Result.Error(e.message ?: R.string.error_occurred.toString())
            }
        }
    }

    fun getStories() : LiveData<PagingData<DetailStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Result<FileUploadResponse> {
        return try {
            val res = apiService.uploadStory(file, description, lat, lon)
            Result.Success(res)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.error_occurred.toString())
        }
    }

    suspend fun getStoryWithLocation(): Result<StoryResponse> {
        return try {
            val response = apiService.getStoriesWithLocation()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.error_occurred.toString())
        }
    }

    fun updateToken(token: String) {
        instance?.let {
            it.apiService = ApiConfig.getApiService(token)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreferences,
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): UserRepository {
            instance?.let { return it }

            return synchronized(this) {
                val newInstance = UserRepository(userPreference, apiService, storyDatabase)
                instance = newInstance
                newInstance
            }
        }
    }
}