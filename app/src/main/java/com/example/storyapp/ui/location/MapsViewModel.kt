package com.example.storyapp.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.Result
import com.example.storyapp.data.response.DetailStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository): ViewModel() {
    private var isDataLoaded = false

    private val _listDetailStory = MutableLiveData<List<DetailStoryItem>>()
    val listDetailStory: LiveData<List<DetailStoryItem>> = _listDetailStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getStoryWithLocation()
            _isLoading.value = false

            when (response) {
                is Result.Success -> {
                    _listDetailStory.value = response.data.listStoryResponse
                    _isError.value = false
                    _errorMessage.value = null
                    isDataLoaded = true
                }

                is Result.Error -> {
                    _isError.value = true
                    _errorMessage.value = response.error
                }

                is Result.Loading -> {
                    _isError.value = false
                    _errorMessage.value = null
                    _isLoading.value = true
                }
            }
        }
    }
}