package com.robby.dicodingstory.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.databinding.StoryItemBinding
import com.robby.dicodingstory.utils.loadImageFromUrl

class ListStoryAdapter :
    PagingDataAdapter<Story, ListStoryAdapter.ViewHolder>(StoryDiffCallback()) {
    private var onClick: OnStoryItemClickListener? = null

    fun setOnStoryItemClickListener(onClick: OnStoryItemClickListener) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryAdapter.ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                tvItemName.text = story.name
                ivItemPhoto.loadImageFromUrl(story.photoUrl)
                ivItemPhoto.contentDescription =
                    root.resources.getString(R.string.picture_story_from, story.name)

                root.setOnClickListener { onClick?.onStoryClick(story) }
            }
        }
    }

    private class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.name == newItem.name && oldItem.photoUrl == newItem.photoUrl
        }
    }

    interface OnStoryItemClickListener {
        fun onStoryClick(story: Story)
    }
}