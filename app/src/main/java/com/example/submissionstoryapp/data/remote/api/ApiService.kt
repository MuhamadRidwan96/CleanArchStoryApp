package com.example.submissionstoryapp.data.remote.api

import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.DetailStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.data.remote.response.SubscribeResponse
import com.example.submissionstoryapp.data.remote.response.UnsubscribeResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.domain.model.SubscribeRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
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
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon:RequestBody? = null
    ): AddStoryResponse

    @Multipart
    @POST("/v1/stories/guest")
    suspend fun addStoriesNoAuth(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon:RequestBody? = null
    ): AddStoryResponse

    @GET("/v1/stories")
    suspend fun getAllStories(
        // Bearer token
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0 // Default value is 0
    ): Response<GetStoriesResponse>

    @GET("/v1/stories/{id}")
    suspend fun getStoryDetail( // Bearer token
        @Path("id") id: String // Story ID
    ): Response<DetailStoryResponse>

    @POST("/v1/notifications/subscribe")
    suspend fun subscribeNotification(
        @Header("Authorization") token: String, // Bearer token
        @Body request: SubscribeRequest
    ): SubscribeResponse

    @DELETE("/v1/notifications/subscribe")
    suspend fun unsubscribeNotification(
        @Header("Authorization") token: String // Bearer token
    ): UnsubscribeResponse
}