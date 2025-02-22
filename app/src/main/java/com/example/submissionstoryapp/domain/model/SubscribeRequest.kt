package com.example.submissionstoryapp.domain.model

import com.google.gson.annotations.SerializedName

data class SubscribeRequest(
    @SerializedName("endpoint") val endpoint: String,
    @SerializedName("keys") val keys: NotificationKeys
)

data class NotificationKeys(
    @SerializedName("p256d") val p256dh: String,
    @SerializedName("auth") val auth: String
)