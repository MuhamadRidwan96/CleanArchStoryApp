package com.example.submissionstoryapp.domain.authUseCase

import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.data.repository.Repository
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.base.UiState
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(private val repository: Repository) {
    operator fun invoke(register: RegisterModel):Flow<UiState<RegisterResponse>>{
        return repository.register(register)
    }
}