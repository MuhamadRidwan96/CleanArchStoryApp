package com.example.submissionstoryapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.domain.authUseCase.CheckLoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.LoginUseCase
import com.example.submissionstoryapp.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkLoginUseCase: CheckLoginUseCase
): ViewModel() {
    private val _loginResult = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginResult: StateFlow<UiState<LoginResponse>> = _loginResult

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> get() = _error

    private var job: Job? = null
    fun login(email: String, password: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _loginResult.value = UiState.Loading
            loginUseCase(email, password).collect { loginState ->
                _loginResult.value = loginState
                if (loginState is UiState.Error) {
                    _error.emit(loginState.message)
                }
            }
        }
    }

    fun checkLogin() {
        viewModelScope.launch {
            val isLoggedIn = checkLoginUseCase()
            _isLoggedIn.value = isLoggedIn
        }
    }
}