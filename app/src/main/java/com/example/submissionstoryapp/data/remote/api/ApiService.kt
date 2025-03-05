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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/v1/register")
    suspend fun register(
        @Body requestRegister: RegisterModel
    ): Response<RegisterResponse>

    @POST("/v1/login")
    suspend fun login(
        @Body requestLogin: LoginModel
    ): Response<LoginResponse>

    @Multipart
    @POST("/v1/stories")
    suspend fun addStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon:RequestBody? = null
    ): Response<AddStoryResponse>

    @GET("/v1/stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): Response<GetStoriesResponse>

    @GET("/v1/stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): Response<DetailStoryResponse>

}