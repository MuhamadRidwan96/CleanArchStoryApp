package com.example.submissionstoryapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.authUseCase.RegisterUseCase
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerResult = MutableStateFlow<UiState<RegisterResponse>>(UiState.Idle)
    val registerResult: StateFlow<UiState<RegisterResponse>> = _registerResult

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> get() = _error

    fun register(register: RegisterModel) {
        viewModelScope.launch {
            registerUseCase(register).collect { registerState ->
                _registerResult.value = registerState
                if (registerState is UiState.Error) {
                    _error.emit(registerState.message)
                }
            }
        }
    }
}
