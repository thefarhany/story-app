package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.Result
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.response.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    private val _userToken = MutableLiveData<UserModel?>()
    val userToken: MutableLiveData<UserModel?> = _userToken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            try {
                val res = repository.saveUser(user)
                _isError.value = false
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val res = repository.login(email, password)
            _isLoading.value = false

            when(res) {
                is Result.Success -> {
                    val name: String = res.data.loginResult!!.name
                    val token: String = res.data.loginResult.token

                    _userToken.value = UserModel(name, token)
                    repository.updateToken(token)

                    saveUser(userToken.value!!)

                    _isError.value = false
                    _errorMessage.value = null
                }

                is Result.Error -> {
                    _isError.value = true
                    _errorMessage.value = res.error
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