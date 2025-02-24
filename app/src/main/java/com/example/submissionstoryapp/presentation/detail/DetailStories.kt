package com.example.submissionstoryapp.presentation.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityDetailStoriesBinding
import com.example.submissionstoryapp.presentation.MainViewModel
import com.example.submissionstoryapp.presentation.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailStories : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoriesBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val id = intent.getStringExtra("id")
        if (id != null) {
            fetchDetailStories(id)
        } else {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show()
        }
        observe()
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDetailStories.collect { detail ->
                    when (detail) {
                        is UiState.Loading -> Unit
                        is UiState.Error -> Unit
                        is UiState.Success -> {
                            binding.apply {
                                Glide.with(imgDetail.context)
                                    .load(detail.data.photoUrl)
                                    .error(R.drawable.ic_gallery)
                                    .into(imgDetail)
                                textViewName.text = detail.data.name
                                tvCreated.text = detail.data.createdAt
                                tvDescription.text = detail.data.description
                                tvCoordinate.text = detail.data.coordinates
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { isError ->
                    Toast.makeText(this@DetailStories, isError, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchDetailStories(id: String) {
        viewModel.getDetailStoriesViewModel(id)
    }
}