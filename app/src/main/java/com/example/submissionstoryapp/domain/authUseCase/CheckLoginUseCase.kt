package com.example.submissionstoryapp.domain.authUseCase

import com.example.submissionstoryapp.utils.UserPref
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckLoginUseCase @Inject constructor(private val userPref: UserPref) {
    suspend operator fun invoke(): Boolean {
        return userPref.getSession().map {
            it.isLogin
        }.firstOrNull() ?: false
    }
}