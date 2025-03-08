package com.example.submissionstoryapp.presentation.story.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityDetailStoriesBinding
import com.example.submissionstoryapp.domain.model.StoryDetailUIModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.story.StoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailStoriesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailStoriesBinding.inflate(layoutInflater) }
    private val viewModel: StoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("id")?.let { fetchDetailStories(it) }
            ?: showMessage(getString(R.string.invalid_event_id))

        setUpObservers()

    }


    private fun setUpObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observeDetail() }
                launch { observerError() }
            }
        }
    }

    private suspend fun observeDetail() {
        viewModel.getDetailStories.collect { state ->
            when (state) {
                is UiState.Success -> {
                    updateUi(state.data)
                    handleLoading(false)
                }
                is UiState.Loading -> handleLoading(true)
                is UiState.Idle -> handleLoading(false)
                is UiState.Error -> handleLoading(false)
            }
        }
    }

    private suspend fun observerError() {
        viewModel.error.collectLatest { showMessage(it) }
    }

    private fun fetchDetailStories(id: String) {
        viewModel.getDetailStoriesViewModel(id)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleLoading(isLoading:Boolean){
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.baseLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun updateUi(data:StoryDetailUIModel)   = with(binding){
        Glide.with(imgDetail.context)
            .load(data.photoUrl)
            .error(R.drawable.ic_gallery)
            .into(imgDetail)

        textViewName.text = data.name
        tvCreated.text = data.createdAt
        tvDescription.text = data.description
        tvCoordinate.text = data.coordinates
    }
}
