package com.robby.dicodingstory.addstory

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityAddStoryBinding
import com.robby.dicodingstory.fragment.LoadingDialogFragment
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var loadingDialog: LoadingDialogFragment

    private val addStoryViewModel by viewModel<AddStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadingDialog = LoadingDialogFragment()

        val storyImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_IMAGE, File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_IMAGE)
        } as? File

        if (storyImage != null) {
            binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(storyImage.path))
        }

        binding.buttonAdd.setOnClickListener {
            if (storyImage != null) {
                val description = binding.edAddDescription.text.toString().trim()

                addStoryViewModel.addStory(storyImage, description).observe(this) {
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
            } else {
                Toast.makeText(this, "Image is null", Toast.LENGTH_SHORT).show()
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

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }
}