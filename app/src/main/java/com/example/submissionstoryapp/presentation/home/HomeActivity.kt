package com.example.submissionstoryapp.presentation.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionstoryapp.databinding.ActivityHomeBinding
import com.example.submissionstoryapp.presentation.MainViewModel
import com.example.submissionstoryapp.presentation.base.MainAdapter
import com.example.submissionstoryapp.presentation.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: MainViewModel by viewModels()

    private val storiesAdapter: MainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Home Activity", "onCreate: Memanggil getStories()")
        viewModel.getStories(page = 1, size = 10, location = 1)
        setUpObserver()
        setUpRecyclerView()
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getStories.collect{ isStories ->
                    when (isStories) {

                        is UiState.Success -> {
                            val storyList = isStories.data.listStory
                            if (storyList.isNotEmpty()) {
                                storiesAdapter.submitList(storyList)
                            } else {
                                Toast.makeText(this@HomeActivity, "Data Kosong", Toast.LENGTH_SHORT)
                                    .show()
                            }
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
                    Toast.makeText(this@HomeActivity, isError, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvFinish.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = storiesAdapter
            addItemDecoration(
                DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            )
        }
    }
}