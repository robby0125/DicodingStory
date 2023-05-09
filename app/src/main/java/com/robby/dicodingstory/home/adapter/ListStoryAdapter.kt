package com.robby.dicodingstory.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.databinding.StoryItemBinding
import com.robby.dicodingstory.utils.loadImageFromUrl

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    private val listStory = ArrayList<Story>()
    private var onClick: OnStoryItemClickListener? = null

    fun setData(listStory: List<Story>) {
        this.listStory.clear()
        this.listStory.addAll(listStory)
    }

    fun setOnStoryItemClickListener(onClick: OnStoryItemClickListener) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryAdapter.ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

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

    interface OnStoryItemClickListener {
        fun onStoryClick(story: Story)
    }
}