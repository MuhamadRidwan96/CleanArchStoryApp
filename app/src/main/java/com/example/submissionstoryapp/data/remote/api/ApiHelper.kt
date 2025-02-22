package com.example.submissionstoryapp.data.remote.api

import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.data.remote.response.SubscribeResponse
import com.example.submissionstoryapp.data.remote.response.UnsubscribeResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.RegisterModel
import retrofit2.Response

interface ApiHelper {

    suspend fun login(requestLogin:LoginModel):Response<LoginResponse>
    suspend fun register(register: RegisterModel): Response<RegisterResponse>
    suspend fun addStories(): AddStoryResponse
    suspend fun noAuthAddStories(): AddStoryResponse
    suspend fun getAllStories(
        page: Int,
        size: Int,
        location: Int
    ): Response<GetStoriesResponse>

    suspend fun subscribeNotification(): SubscribeResponse
    suspend fun unSubscribeNotification(): UnsubscribeResponse
}