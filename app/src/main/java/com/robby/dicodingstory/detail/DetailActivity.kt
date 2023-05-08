package com.robby.dicodingstory.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityDetailBinding
import com.robby.dicodingstory.utils.loadImageFromUrl
import com.robby.dicodingstory.utils.withDateFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModel<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            detailViewModel.getDetailStory(id).observe(this) {
                when (it) {
                    is Resource.Success -> showDetail(it.data)
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun showDetail(story: Story?) {
        if (story != null) {
            val roundedCorner = resources.getDimensionPixelSize(R.dimen.cardCornerRadius) / 2
            val options = RequestOptions().transform(FitCenter(), RoundedCorners(roundedCorner))

            binding.imgStory.loadImageFromUrl(story.photoUrl, options)
            binding.tvUserName.text = story.name
            binding.tvDate.text = story.createdAt.withDateFormat()
            binding.tvDescription.text = story.description
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}