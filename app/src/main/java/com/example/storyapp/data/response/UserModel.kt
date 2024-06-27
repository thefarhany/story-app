package com.example.storyapp.data.response

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
