package com.robby.dicodingstory.storymap

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
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

        setMapStyle()
        storyMapViewModel.getAllStoriesWithLocation().observe(this) {
            when (it) {
                is Resource.Success -> {
                    setLoading(false)
                    provideStoryMarker(it.data)
                }

                is Resource.Loading -> setLoading(true)

                is Resource.Error -> {
                    setLoading(false)
                    Toast.makeText(
                        this,
                        getString(R.string.error_loading_story_map),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun provideStoryMarker(stories: List<Story>?) {
        if (stories.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.no_story_with_location), Toast.LENGTH_SHORT)
                .show()
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

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

            if (!success) {
                Log.e(TAG, "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find map style. Error : $e")
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        mapFragment.view?.isVisible = !isLoading
    }

    private companion object {
        const val TAG = "StoryMapActivity"
    }
}