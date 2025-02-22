package com.example.submissionstoryapp.domain.authUseCase

import com.example.submissionstoryapp.utils.UserPref
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val userPref: UserPref) {
    suspend operator fun invoke(){
        userPref.logout()
    }
}