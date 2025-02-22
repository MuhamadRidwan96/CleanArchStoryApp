package com.example.submissionstoryapp.domain.authUseCase

import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.UserModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.utils.UserPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val repository: Repository,
    private val userPref: UserPref
) {
    operator fun invoke(email: String, password: String): Flow<UiState<LoginResponse>> = flow {

        repository.login(LoginModel(email, password)).collect { uiState ->

            when (uiState) {
                is UiState.Success -> {
                    userPref.saveSession(
                        UserModel(
                            email = email,
                            token = uiState.data.loginResult.token,
                            isLogin = true
                        )
                    )
                    emit(UiState.Success(uiState.data))
                }
                is UiState.Error -> emit(UiState.Error(uiState.message))
                else -> Unit
            }
        }
    }
}

