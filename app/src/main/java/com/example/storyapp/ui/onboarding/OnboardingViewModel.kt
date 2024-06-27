package com.example.storyapp.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.UserModel

class OnboardingViewModel(private val repository: UserRepository): ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return repository.getUser().asLiveData()
    }
}