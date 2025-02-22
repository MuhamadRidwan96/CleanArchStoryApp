package com.example.submissionstoryapp.data.remote.response

import com.example.submissionstoryapp.domain.model.NotificationKeys

data class SubscribeResponse(
    val error: Boolean,
    val message: String,
    val data: SubscriptionData
)

data class SubscriptionData(
    val id: String,
    val endpoint: String,
    val keys: NotificationKeys,
    val userId: String,
    val createdAt: String
)