package com.example.submissionstoryapp.domain

import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.presentation.base.UiState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoriesUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null

    ): Flow<UiState<AddStoryResponse>> {
        return repository.addStories(description, photo, lat, lon)
    }

}