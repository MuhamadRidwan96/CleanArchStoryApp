package com.example.submissionstoryapp.presentation.base

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.response.Story
import com.example.submissionstoryapp.databinding.ListStoryBinding
import com.example.submissionstoryapp.presentation.detail.DetailStories

class MainAdapter : ListAdapter<Story, MainAdapter.MainViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
                oldItem == newItem
        }
    }

    class MainViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                textViewName.text = story.name

                Glide.with(binding.imgStories.context)
                    .load(story.photoUrl)
                    .error(R.drawable.perm_media_24px)
                    .into(binding.imgStories)
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailStories::class.java)
                intent.putExtra("id", story.id)
                context.startActivity(intent)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("MainAdapter", "Binding item: ${item.name}")
        holder.bind(item)
    }

}