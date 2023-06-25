package com.robby.dicodingstory.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.robby.dicodingstory.R
import com.robby.dicodingstory.databinding.ActivityCameraBinding
import com.robby.dicodingstory.fragment.LoadingDialogFragment
import com.robby.dicodingstory.utils.EspressoIdlingResource
import com.robby.dicodingstory.utils.Helper

class CameraActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var loadingDialog: LoadingDialogFragment

    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                selectedImg.let { uri ->
                    val myFile = Helper.uriToFile(uri, this@CameraActivity)
                    val reducedFile = Helper.reduceFileImage(myFile)

                    val intent = Intent(this@CameraActivity, AddStoryActivity::class.java)
                    intent.putExtra(AddStoryActivity.EXTRA_IMAGE, reducedFile)
                    startActivity(intent)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loadingDialog = LoadingDialogFragment()

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
        } else {
            startCamera()
        }

        binding.btnGallery.setOnClickListener(this)
        binding.btnCapture.setOnClickListener(this)
        binding.btnRotateCamera.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, "Don't have camera permission.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                startCamera()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_gallery -> pickImageFromGallery()
            R.id.btn_capture -> takePhoto()
            R.id.btn_rotate_camera -> rotateCamera()
        }
    }

    private fun allPermissionGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to show camera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePhoto() {
        imageCapture?.let {
            EspressoIdlingResource.increment()

            val photoFile = Helper.createFile(application)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            val bundle = Bundle()
            bundle.putString(
                LoadingDialogFragment.EXTRA_LOADING_MESSAGE,
                getString(R.string.capture_process)
            )

            loadingDialog.arguments = bundle
            loadingDialog.show(supportFragmentManager, "Loading Dialog")

            it.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val isBackCamera = cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                        Helper.rotateFile(photoFile, isBackCamera)

                        val reducedFile = Helper.reduceFileImage(photoFile)
                        Log.d("Helper", "onImageSaved: ${reducedFile.length()}")

                        loadingDialog.dismiss()

                        EspressoIdlingResource.decrement()

                        val intent = Intent(this@CameraActivity, AddStoryActivity::class.java)
                        intent.putExtra(AddStoryActivity.EXTRA_IMAGE, reducedFile)
                        startActivity(intent)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            this@CameraActivity,
                            "Error when taking picture.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun rotateCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        startCamera()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}