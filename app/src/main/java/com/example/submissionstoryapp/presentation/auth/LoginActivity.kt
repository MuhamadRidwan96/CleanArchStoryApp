package com.example.submissionstoryapp.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.databinding.ActivityLoginBinding
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.home.HomeActivity
import com.example.submissionstoryapp.presentation.signup.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBinding()
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
                Snackbar.make(
                    binding.root,
                    "Email and password cannot be empty",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initializeBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collectLatest { loginState ->
                    when (loginState) {
                        is UiState.Success -> {
                            binding.loginButton.setLoading(false)
                            navigateToHomeScreen()
                            showToast("Login Success!")
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
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
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


