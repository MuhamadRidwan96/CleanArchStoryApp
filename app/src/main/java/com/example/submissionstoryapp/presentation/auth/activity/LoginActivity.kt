package com.example.submissionstoryapp.presentation.auth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityLoginBinding
import com.example.submissionstoryapp.presentation.auth.AuthViewModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.signup.activity.SignUpActivity
import com.example.submissionstoryapp.presentation.story.activity.StoriesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpListener()
        setUpObserver()
    }

    private fun setUpListener() {
        binding.signupButton.setOnClickListener {
            navigateToSignUpActivity()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.loginButton.setLoading(true)
                lifecycleScope.launch {
                    viewModel.login(email, password)
                }
            } else {
              showToast(getString(R.string.email_and_password_cannot_be_empty))
            }
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collectLatest { loginState ->
                    when (loginState) {
                        is UiState.Success -> {
                            binding.loginButton.setLoading(false)
                            navigateToHomeScreen()
                            showToast(getString(R.string.login_success))
                        }

                        is UiState.Error -> {
                            binding.loginButton.setLoading(false)
                            showToast(loginState.message)
                        }

                        is UiState.Loading -> {
                            binding.loginButton.setLoading(true)
                        }

                        is UiState.Idle -> {
                            binding.loginButton.setLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this@LoginActivity, StoriesActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
