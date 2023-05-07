package com.robby.dicodingstory.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityHomeBinding
import com.robby.dicodingstory.home.adapter.ListStoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.rvStories) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }

        homeViewModel.allStories.observe(this) {
            when (it) {
                is Resource.Success -> populateStories(it.data)
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
    }

    private fun populateStories(stories: List<Story>?) {
        if (stories.isNullOrEmpty()) {
            // TODO : Do something when stories is empty or null
        } else {
            val adapter = ListStoryAdapter()
            adapter.setData(stories)

            binding.rvStories.adapter = adapter
        }
    }
}