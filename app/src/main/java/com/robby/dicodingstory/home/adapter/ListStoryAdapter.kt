package com.robby.dicodingstory.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.databinding.StoryItemBinding
import com.robby.dicodingstory.utils.loadImageFromUrl

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    private val listStory = ArrayList<Story>()

    fun setData(listStory: List<Story>) {
        this.listStory.clear()
        this.listStory.addAll(listStory)
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
                tvUserName.text = story.name

                val roundedCorner =
                    root.resources.getDimensionPixelSize(R.dimen.cardCornerRadius) / 2
                val options =
                    RequestOptions().transform(CenterCrop(), RoundedCorners(roundedCorner))
                imgStory.loadImageFromUrl(story.photoUrl, options)
            }
        }
    }
}