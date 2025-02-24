package com.example.submissionstoryapp.data.remote.api

import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.DetailStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.data.remote.response.SubscribeResponse
import com.example.submissionstoryapp.data.remote.response.UnsubscribeResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun login(requestLogin: LoginModel): Response<LoginResponse> {
        return apiService.login(requestLogin)
    }

    override suspend fun register(register: RegisterModel): Response<RegisterResponse> {
        return apiService.register(register)
    }


    override suspend fun addStories(): AddStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun noAuthAddStories(): AddStoryResponse {
        TODO("Not yet implemented")
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


    override suspend fun subscribeNotification(): SubscribeResponse {
        TODO("Not yet implemented")
    }

    override suspend fun unSubscribeNotification(): UnsubscribeResponse {
        TODO("Not yet implemented")
    }
}