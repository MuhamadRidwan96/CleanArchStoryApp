package com.example.submissionstoryapp.data.repository

import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.DetailStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.base.UiState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface Repository {

    fun login(requestLogin: LoginModel): Flow<UiState<LoginResponse>>
    fun register(register: RegisterModel): Flow<UiState<RegisterResponse>>
    fun getAllStories(
        page: Int,
        size: Int,
        location: Int
    ): Flow<UiState<GetStoriesResponse>>
    fun detailStories(id:String) : Flow<UiState<DetailStoryResponse>>
    fun addStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody ? = null,
        lon: RequestBody ? = null
    ) : Flow<UiState<AddStoryResponse>>
}