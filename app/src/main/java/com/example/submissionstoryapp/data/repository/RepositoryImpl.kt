package com.example.submissionstoryapp.data.repository

import com.example.submissionstoryapp.data.remote.api.ApiHelper
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.model.LoginModel
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.utils.Constant
import com.example.submissionstoryapp.utils.ErrorHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) : Repository {
    override fun login(requestLogin: LoginModel): Flow<UiState<LoginResponse>> = flow {
        try {

            val responseLogin = apiHelper.login(requestLogin)

            if (responseLogin.isSuccessful) {
                emit(responseLogin.body()?.let { UiState.Success(it) }
                    ?: UiState.Error(Constant.ERROR_NULL))
            } else {
                val errorBody = responseLogin.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { ErrorHandle.parseErrorMessage(it) } ?: Constant.UNKNOWN_ERROR
                } catch (e: Exception) {
                    Constant.FAILED_PARSE
                }
                emit(UiState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Network Error : ${e.localizedMessage}"))
        }
    }

    override fun register(register: RegisterModel): Flow<UiState<RegisterResponse>> = flow {
        try {
            val response = apiHelper.register(register)
            if (response.isSuccessful) {
                emit(response.body()?.let { UiState.Success(it) }
                    ?: UiState.Error(Constant.ERROR_NULL))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { ErrorHandle.parseErrorMessage(it) } ?: Constant.UNKNOWN_ERROR
                } catch (e: Exception) {
                    Constant.FAILED_PARSE
                }
                emit(UiState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Network Error : ${e.localizedMessage}"))
        }
    }

    override fun getAllStories(
        page: Int,
        size: Int,
        location: Int
    ): Flow<UiState<GetStoriesResponse>> = flow {
        try {
            val getStoriesResponse = apiHelper.getAllStories(page, size, location)

            if (getStoriesResponse.isSuccessful) {
               emit(getStoriesResponse.body()?.let { UiState.Success(it) } ?: UiState.Error(Constant.ERROR_NULL))
            } else {
                val errorBody = getStoriesResponse.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { ErrorHandle.parseErrorMessage(it) } ?: Constant.UNKNOWN_ERROR
                } catch (e: Exception) {
                    Constant.FAILED_PARSE
                }
                emit(UiState.Error(errorMessage))
            }

        } catch (e: Exception) {
            emit(UiState.Error("Network Error : ${e.localizedMessage}"))
        }
    }
}
