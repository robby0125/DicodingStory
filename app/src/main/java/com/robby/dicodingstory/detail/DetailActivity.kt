package com.robby.dicodingstory.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

        supportActionBar?.title = getString(R.string.detail_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.detailView.visibility = View.INVISIBLE
        binding.error.root.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            detailViewModel.getDetailStory(id).observe(this) {
                when (it) {
                    is Resource.Success -> {
                        showLoading(false)
                        showDetail(it.data)
                    }

                    is Resource.Loading -> {
                        showLoading(true)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        showError(it.message ?: getString(R.string.error_default_message))
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return false
    }

    private fun showDetail(story: Story?) {
        if (story != null) {
            val options = RequestOptions().transform(FitCenter(), RoundedCorners(16))

            binding.detailView.visibility = View.VISIBLE
            binding.ivDetailPhoto.loadImageFromUrl(story.photoUrl, options)
            binding.tvDetailName.text = story.name
            binding.tvDate.text = story.createdAt.withDateFormat()
            binding.tvDetailDescription.text = story.description
        } else {
            showError(getString(R.string.empty))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(message: String) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = message.trim()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}