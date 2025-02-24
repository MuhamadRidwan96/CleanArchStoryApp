package com.example.submissionstoryapp.data.remote.response

data class StoryDetailResponse(
    val error: Boolean,
    val message: String,
    val story: StoryDetail
)

data class StoryDetail(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,
    val lon: Double?
)

