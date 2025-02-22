package com.example.submissionstoryapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.databinding.ActivityLoginBinding
import com.example.submissionstoryapp.presentation.MainViewModel
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

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpObserver()
        binding.signupButton.setOnClickListener {
            navigateToSignUpActivity()
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (password.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.login(email.toString(), password)
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Email and password cannot be empty",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collectLatest { loginState ->
                    Log.d("LoginResult", "Collected login state: $loginState")
                    when (loginState) {
                        is UiState.Success -> {
                            navigateToHomeScreen()
                            Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_LONG)
                                .show()
                        }

                        is UiState.Error -> Unit

                        is UiState.Loading -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { isError ->
                    Toast.makeText(this@LoginActivity, isError, Toast.LENGTH_SHORT).show()
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
}


