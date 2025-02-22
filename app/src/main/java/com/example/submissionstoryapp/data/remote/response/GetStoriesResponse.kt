package com.example.submissionstoryapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)

data class Story(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?
)