package com.example.submissionstoryapp.utils

import android.annotation.SuppressLint
import org.json.JSONObject

object ErrorHandle {
    @SuppressLint("SuspiciousIndentation")
    fun parseErrorMessage(errorBody: String): String? {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("message")
        } catch (e: Exception) {
            null
        }
    }
}
