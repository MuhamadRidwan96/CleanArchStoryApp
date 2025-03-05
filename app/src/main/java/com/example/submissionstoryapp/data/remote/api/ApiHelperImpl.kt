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
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun login(requestLogin: LoginModel): Response<LoginResponse> {
        return apiService.login(requestLogin)
    }

    override suspend fun register(register: RegisterModel): Response<RegisterResponse> {
        return apiService.register(register)
    }

    override suspend fun addStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Response<AddStoryResponse> {
        return apiService.addStories(description, photo, lat, lon)
    }


    override suspend fun getAllStories(
        page: Int,
        size: Int,
        location: Int
    ): Response<GetStoriesResponse> {
        return apiService.getAllStories(page, size, location)
    }

    override suspend fun getDetailStories(id: String): Response<DetailStoryResponse> {
        return apiService.getStoryDetail(id)
    }

}