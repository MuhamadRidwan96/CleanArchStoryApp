package com.example.submissionstoryapp.presentation.story.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.response.Story
import com.example.submissionstoryapp.databinding.ActivityHomeBinding
import com.example.submissionstoryapp.presentation.auth.activity.LoginActivity
import com.example.submissionstoryapp.presentation.base.MainAdapter
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.story.StoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StoriesActivity @Inject constructor() : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: StoriesViewModel by viewModels()
    private val storiesAdapter by lazy { MainAdapter() }

    private val scrollHandler = Handler(Looper.getMainLooper())
    private val scrollRunnable = Runnable { binding.extendedFab.extend() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpObservers()
        setUpUI()
    }

    private fun setUpUI() {
        setupToolbar()
        setupFabClickListener()
        setUpRecyclerView()
        setupFabScrollBehavior()
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { observerStories() }
                launch { observerError() }
            }
        }
    }

    private fun setupFabClickListener() {
        binding.extendedFab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setUpRecyclerView() {
        binding.rvFinish.apply {
            layoutManager = LinearLayoutManager(this@StoriesActivity)
            adapter = storiesAdapter
            addItemDecoration(
                DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            )
        }
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

    private suspend fun observerStories() {
        viewModel.getStories(page = 1, size = 20, location = 1)
        viewModel.getStories.collect { isStories ->
            when (isStories) {
                is UiState.Success -> handleSuccess(isStories.data.listStory)
                is UiState.Error -> Unit
                is UiState.Loading -> Unit
                is UiState.Idle -> Unit
            }
        }
    }

    private suspend fun observerError() {
        viewModel.error.collectLatest { showToast(it) }
    }

    private fun handleSuccess(storyList: List<Story>) {
        if (storyList.isNotEmpty()) {
            val sortedList = storyList.sortedByDescending { it.createdAt }
            storiesAdapter.submitList(sortedList)
        } else {
            showToast(getString(R.string.data_kosong))
        }
    }

    private fun handleLogout() {
        viewModel.logout()
        val intent = Intent(this@StoriesActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupFabScrollBehavior() {
        binding.rvFinish.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) binding.extendedFab.shrink()
                else if (dy < 0) binding.extendedFab.shrink()
                else binding.extendedFab.extend()

                scrollHandler.removeCallbacks(scrollRunnable)
                scrollHandler.postDelayed(scrollRunnable, 1000)
            }
        })
    }
}
