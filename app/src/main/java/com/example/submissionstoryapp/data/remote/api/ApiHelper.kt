package com.example.submissionstoryapp.data.remote.api

import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.DetailStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.RegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface ApiHelper {

    suspend fun login(requestLogin:LoginModel):Response<LoginResponse>
    suspend fun register(register: RegisterModel): Response<RegisterResponse>
    suspend fun addStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Response<AddStoryResponse>
    suspend fun getAllStories(
        page: Int,
        size: Int,
        location: Int
    ): Response<GetStoriesResponse>
    suspend fun getDetailStories(id:String): Response<DetailStoryResponse>
}