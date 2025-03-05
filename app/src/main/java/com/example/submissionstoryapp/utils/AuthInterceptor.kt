package com.example.submissionstoryapp.utils

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userPref: UserPref) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userPref.getSession().firstOrNull()?.token
        } ?: throw IllegalStateException("Token is not available. Please login first.")
        Log.d("TOKEN_CHECK", "Token di Interceptor: $token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}