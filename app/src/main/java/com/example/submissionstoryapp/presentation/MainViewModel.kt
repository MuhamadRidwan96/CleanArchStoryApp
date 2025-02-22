package com.example.submissionstoryapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.StoriesUseCase
import com.example.submissionstoryapp.domain.authUseCase.CheckLoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.LoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.LogoutUseCase
import com.example.submissionstoryapp.domain.authUseCase.RegisterUseCase
import com.example.submissionstoryapp.domain.model.RegisterModel
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
class MainViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val storiesUseCase: StoriesUseCase,
    private val checkLoginUseCase: CheckLoginUseCase
) : ViewModel() {
    private val _loginResult = MutableStateFlow<UiState<LoginResponse>>(UiState.Loading)
    val loginResult: StateFlow<UiState<LoginResponse>> = _loginResult

    private val _registerResult = MutableStateFlow<UiState<RegisterResponse>>(UiState.Loading)
    val registerResult: StateFlow<UiState<RegisterResponse>> = _registerResult

    private val _getStories = MutableStateFlow<UiState<GetStoriesResponse>>(UiState.Loading)
    val getStories: StateFlow<UiState<GetStoriesResponse>> = _getStories

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _error = MutableSharedFlow<String>(replay = 0)
    val error: SharedFlow<String> get() = _error

    private var job: Job? = null
    fun login(email: String, password: String) {
        job?.cancel()
        job = viewModelScope.launch {
            loginUseCase(email, password).collect { loginState ->
                _loginResult.value = loginState

                if (loginState is UiState.Error) {
                    _error.emit(loginState.message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun checkLogin() {
        viewModelScope.launch {
            val isLoggedIn = checkLoginUseCase()
            _isLoggedIn.value = isLoggedIn
        }
    }

    //Register View Model
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

    //Get stories view model
    fun getStories(
        page: Int,
        size: Int,
        location: Int
    ) {
        viewModelScope.launch {
            storiesUseCase(page, size, location).collect { stories ->
                _getStories.value = stories
                if (stories is UiState.Error) {
                    _error.emit(stories.message)
                }
            }
        }
    }
}