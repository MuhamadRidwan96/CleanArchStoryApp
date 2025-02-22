package com.example.submissionstoryapp.presentation.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.data.remote.response.RegisterResponse
import com.example.submissionstoryapp.databinding.ActivitySignUpBinding
import com.example.submissionstoryapp.domain.model.RegisterModel
import com.example.submissionstoryapp.presentation.MainViewModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpObserver()

        binding.signupButton.setOnClickListener {
            registerAction()
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResult.collect { registerState ->
                    when (registerState) {
                        is UiState.Error -> { errorHandle(registerState.message) }

                        is UiState.Success -> { handleSuccess(registerState.data) }

                        else -> Unit
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
            viewModel.register(RegisterModel(name, email, password))
        }
    }

    private fun errorHandle(errorMessage: String) {
        Snackbar.make(binding.root, "Error: $errorMessage", Snackbar.LENGTH_LONG).show()
    }

    private fun handleSuccess(response: RegisterResponse) {
       Snackbar.make(binding.root,response.message,Snackbar.LENGTH_SHORT).show()

        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}