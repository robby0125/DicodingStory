package com.robby.dicodingstory.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityAddStoryBinding
import com.robby.dicodingstory.fragment.LoadingDialogFragment
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var storyImage: File? = null
    private var lat: Double? = null
    private var lon: Double? = null

    private val addStoryViewModel by viewModel<AddStoryViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLocation()

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyLocation()

                else -> {
                    binding.cbPostLocation.isChecked = false
                    Toast.makeText(
                        this@AddStoryActivity,
                        getString(R.string.location_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadingDialog = LoadingDialogFragment()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        storyImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_IMAGE, File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_IMAGE)
        } as? File

        storyImage?.let { binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(it.path)) }

        with(binding) {
            cbPostLocation.setOnClickListener(this@AddStoryActivity)
            buttonAdd.setOnClickListener(this@AddStoryActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_add -> uploadStory()
            R.id.cb_post_location -> {
                if (binding.cbPostLocation.isChecked) getMyLocation()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    binding.cbPostLocation.isChecked = false
                    Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun uploadStory() {
        storyImage?.let { img ->
            val description = binding.edAddDescription.text.toString().trim()

            addStoryViewModel.addStory(img, description, lat, lon).observe(this) {
                when (it) {
                    is Resource.Success -> {
                        val homeIntent = Intent(this@AddStoryActivity, HomeActivity::class.java)
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(homeIntent)
                    }

                    is Resource.Loading -> {
                        val bundle = Bundle()
                        bundle.putString(
                            LoadingDialogFragment.EXTRA_LOADING_MESSAGE,
                            getString(R.string.upload_process)
                        )

                        loadingDialog.arguments = bundle
                        loadingDialog.show(supportFragmentManager, "Loading Dialog")
                    }

                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            this@AddStoryActivity,
                            it.message ?: getString(R.string.error_default_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Image is null", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }
}