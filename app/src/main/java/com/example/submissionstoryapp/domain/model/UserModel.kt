package com.example.submissionstoryapp.domain.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String,
    val isLogin: Boolean = false
)
