package com.robby.dicodingstory.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.robby.dicodingstory.R
import com.robby.dicodingstory.addstory.CameraActivity
import com.robby.dicodingstory.authentication.AuthViewModel
import com.robby.dicodingstory.authentication.LoginActivity
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityHomeBinding
import com.robby.dicodingstory.detail.DetailActivity
import com.robby.dicodingstory.home.adapter.ListStoryAdapter
import com.robby.dicodingstory.storymap.StoryMapActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity(), ListStoryAdapter.OnStoryItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModel<HomeViewModel>()
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_name)

        with(binding.rvStories) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }

        binding.rvStories.visibility = View.INVISIBLE
        binding.error.root.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE

        homeViewModel.getAllStories().observe(this) {
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    populateStories(it.data)
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

        binding.fabAddStory.setOnClickListener {
            val cameraIntent = Intent(this, CameraActivity::class.java)
            startActivity(cameraIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_story_map -> {
                val storyMapIntent = Intent(this@HomeActivity, StoryMapActivity::class.java)
                startActivity(storyMapIntent)

                true
            }

            R.id.action_logout -> {
                showLogoutConfirmation()
                true
            }

            else -> false
        }
    }

    override fun onStoryClick(story: Story) {
        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_ID, story.id)
        startActivity(detailIntent)
    }

    private fun populateStories(stories: List<Story>?) {
        if (stories.isNullOrEmpty()) {
            showError(getString(R.string.empty))
        } else {
            val adapter = ListStoryAdapter()
            adapter.setData(stories)
            adapter.setOnStoryItemClickListener(this)

            binding.rvStories.visibility = View.VISIBLE
            binding.rvStories.adapter = adapter
        }
    }

    private fun showLogoutConfirmation() {
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation))
            .setCancelable(true)
            .setPositiveButton(R.string.yes) { _, _ ->
                authViewModel.logout()

                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginIntent)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(message: String) {
        binding.error.root.visibility = View.VISIBLE
        binding.error.tvErrorMessage.text = message.trim()
    }
}