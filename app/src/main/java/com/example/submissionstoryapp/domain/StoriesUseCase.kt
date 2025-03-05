package com.example.submissionstoryapp.domain

import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.presentation.base.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoriesUseCase @Inject constructor(
    private val repository: Repository
) {
   operator fun invoke(
        page: Int,
        size: Int,
        location: Int
    ): Flow<UiState<GetStoriesResponse>> {
        return repository.getAllStories(page, size, location)
    }
}