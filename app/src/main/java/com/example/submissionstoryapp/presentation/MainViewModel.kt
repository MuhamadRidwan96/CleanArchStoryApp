package com.example.submissionstoryapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.remote.response.AddStoryResponse
import com.example.submissionstoryapp.data.remote.response.GetStoriesResponse
import com.example.submissionstoryapp.data.remote.response.LoginResponse
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.domain.AddStoriesUseCase
import com.example.submissionstoryapp.domain.DetailStoriesUseCase
import com.example.submissionstoryapp.domain.StoriesUseCase
import com.example.submissionstoryapp.domain.authUseCase.CheckLoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.LoginUseCase
import com.example.submissionstoryapp.domain.authUseCase.LogoutUseCase
import com.example.submissionstoryapp.domain.authUseCase.RegisterUseCase
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.domain.model.StoryDetailUIModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val storiesUseCase: StoriesUseCase,
    private val checkLoginUseCase: CheckLoginUseCase,
    private val detailStoriesUseCase: DetailStoriesUseCase,
    private val addStoriesUseCase: AddStoriesUseCase
) : ViewModel() {
    private val _loginResult = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginResult: StateFlow<UiState<LoginResponse>> = _loginResult

    private val _registerResult = MutableStateFlow<UiState<RegisterResponse>>(UiState.Idle)
    val registerResult: StateFlow<UiState<RegisterResponse>> = _registerResult

    private val _getStories = MutableStateFlow<UiState<GetStoriesResponse>>(UiState.Loading)
    val getStories: StateFlow<UiState<GetStoriesResponse>> = _getStories

    private val _getDetailStories = MutableStateFlow<UiState<StoryDetailUIModel>>(UiState.Loading)
    val getDetailStories: StateFlow<UiState<StoryDetailUIModel>> = _getDetailStories

    private val _addStories = MutableStateFlow<UiState<AddStoryResponse>>(UiState.Idle)
    val addStories: SharedFlow<UiState<AddStoryResponse>> = _addStories

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

    // Get detail stories view model
    fun getDetailStoriesViewModel(id: String) {
        viewModelScope.launch {
            detailStoriesUseCase(id).collect { detail ->
                val storyDetail = detailStoriesUseCase.processStoryDetail(detail)
                _getDetailStories.value = storyDetail
                if (detail is UiState.Error) {
                    _error.emit(detail.message)
                }
            }
        }
    }

    fun addStoriesViewModel(description: String, photoFile: File, lat: Float?, lon: Float?) {
        viewModelScope.launch {
            val descriptionRequestBody = description.toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
            val photoPart = photoFile.asMultipartBodyPart("photo")
            val latRequestBody = lat?.toString()?.toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
            val lonRequestBody = lon?.toString()?.toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())

            addStoriesUseCase(descriptionRequestBody, photoPart, latRequestBody, lonRequestBody).collect { isAdd ->
                _addStories.value = isAdd
                if (isAdd is UiState.Error) {
                    _error.emit(isAdd.message)
                }
            }
        }
    }

    private fun File.asMultipartBodyPart(partName: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            partName,
            this.name,
            this.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }

}