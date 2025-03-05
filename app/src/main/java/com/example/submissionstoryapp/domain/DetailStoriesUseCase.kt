package com.example.submissionstoryapp.domain

import com.example.submissionstoryapp.data.remote.response.DetailStoryResponse
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.domain.model.StoryDetailUIModel
import com.example.submissionstoryapp.presentation.base.UiState
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class DetailStoriesUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(id: String): Flow<UiState<DetailStoryResponse>> {
        return repository.detailStories(id)
    }

    private fun formatDate(createdAt: String): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val date = ZonedDateTime.parse(createdAt)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())
            date.format(formatter)

        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            date?.let { outputFormat.format(it) } ?: "-"

        }
    }

    private fun formatCoordinates(lat: Double?, lon: Double?): String {
        return if (lat != null && lon != null) {
            "Lat : %.3f ,   Lon : %.3f".format(lat, lon)
        } else {
            "Koordinat tidak tersedia"
        }
    }

    fun processStoryDetail(story: UiState<DetailStoryResponse>)
            : UiState<StoryDetailUIModel >{
        return when(story){
            is UiState.Success -> {UiState.Success(
                StoryDetailUIModel(name = story.data.story.name,
                description = story.data.story.description,
                photoUrl = story.data.story.photoUrl,
                createdAt = formatDate(story.data.story.createdAt),
                coordinates = formatCoordinates(story.data.story.lat, story.data.story.lon)))
            }
            is UiState.Error -> UiState.Error(story.message)
            is UiState.Loading -> UiState.Loading
            is UiState.Idle -> UiState.Idle
        }
    }
}