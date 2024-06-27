package com.example.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.Result.*
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.DetailStoryItem
import com.example.storyapp.data.response.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {
    val getStories: LiveData<PagingData<DetailStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}