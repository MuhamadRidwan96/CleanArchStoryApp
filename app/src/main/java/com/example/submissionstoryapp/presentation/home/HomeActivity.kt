package com.example.submissionstoryapp.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityHomeBinding
import com.example.submissionstoryapp.presentation.MainViewModel
import com.example.submissionstoryapp.presentation.base.MainAdapter
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.login.LoginActivity
import com.example.submissionstoryapp.presentation.story.AddStoryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity @Inject constructor() : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: MainViewModel by viewModels()

    private val storiesAdapter: MainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeBinding()
        initializeViewModel()
        setupToolbar()
        setupFabClickListener()
        setUpRecyclerView()

    }

    private fun setupFabClickListener() {
        binding.extendedFab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initializeViewModel() {
        viewModel.getStories(page = 1, size = 20, location = 1)
        setUpObserver()
    }

    private fun initializeBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_home_fragment -> {
                handleLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getStories.collect { isStories ->
                    when (isStories) {
                        is UiState.Success -> {
                            val storyList = isStories.data.listStory
                            if (storyList.isNotEmpty()) {
                                val sortedList = storyList.sortedByDescending { it.createdAt }
                                storiesAdapter.submitList(sortedList)
                            } else {
                              showToast("Data kosong!")
                            }
                        }

                        is UiState.Error -> Unit
                        is UiState.Loading -> Unit
                        is UiState.Idle -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { isError ->
                   showToast(isError)
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

    private fun handleLogout() {
        viewModel.logout()
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}