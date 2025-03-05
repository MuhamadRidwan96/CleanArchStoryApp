package com.example.submissionstoryapp.domain.model

import java.io.File

data class Story(
    val description: String,
    val photo: File,
    val lat: Float?,
    val lon: Float?
)
