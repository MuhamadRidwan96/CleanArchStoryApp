package com.example.submissionstoryapp.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.presentation.home.HomeActivity
import com.example.submissionstoryapp.presentation.auth.LoginActivity
import com.example.submissionstoryapp.presentation.auth.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        lifecycleScope.launch {
            delay(2000)
            checkLoginObserver()
            viewModel.checkLogin()
        }
    }

    private fun checkLoginObserver() {
        viewModel.isLoggedIn.observe(this) { isLogin ->
            if (isLogin) {
                startActivity(Intent(this,HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
}