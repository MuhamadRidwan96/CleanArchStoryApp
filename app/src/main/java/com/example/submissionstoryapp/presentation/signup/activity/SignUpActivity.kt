package com.example.submissionstoryapp.presentation.signup.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.databinding.ActivitySignUpBinding
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.auth.activity.LoginActivity
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val binding by lazy {  ActivitySignUpBinding.inflate(layoutInflater) }
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpObserver()
        setUpListener()
    }

    private fun setUpListener() {
        binding.signupButton.setOnClickListener {
            registerAction()
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResult.collect { registerState ->
                    when (registerState) {
                        is UiState.Error -> {
                            binding.signupButton.setUpSignUp(false)
                            errorHandle(registerState.message)
                        }

                        is UiState.Success -> {
                            binding.signupButton.setUpSignUp(false)
                            handleSuccess(registerState.data)
                        }

                        is UiState.Loading -> binding.signupButton.setUpSignUp(true)
                        is UiState.Idle -> binding.signupButton.setUpSignUp(false)
                    }
                }
            }
        }
    }

    private fun registerAction() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            binding.signupButton.setUpSignUp(true)
            viewModel.register(RegisterModel(name, email, password))
        }
    }

    private fun errorHandle(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(response: RegisterResponse) {
        errorHandle(response.message)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
