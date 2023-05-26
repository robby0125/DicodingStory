package com.robby.dicodingstory.storymap

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityStoryMapBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapBinding
    private lateinit var mapFragment: SupportMapFragment

    private val storyMapViewModel by viewModel<StoryMapViewModel>()
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.story_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        storyMapViewModel.getAllStoriesWithLatLng().observe(this) {
            when (it) {
                is Resource.Success -> {
                    setLoading(false)
                    provideStoryMarker(it.data)
                }

                is Resource.Loading -> setLoading(true)

                is Resource.Error -> {}
            }
        }
    }

    private fun provideStoryMarker(stories: List<Story>?) {
        if (stories.isNullOrEmpty()) {
            Log.d("StoryMapActivity", "provideStoryMarker: empty or null list")
        } else {
            stories.forEach { story ->
                if (story.lat != null && story.lon != null) {
                    val latLng = LatLng(story.lat, story.lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(story.name)
                            .snippet(story.description)
                    )
                    boundsBuilder.include(latLng)
                }
            }

            val bounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        mapFragment.view?.isVisible = !isLoading
    }
}