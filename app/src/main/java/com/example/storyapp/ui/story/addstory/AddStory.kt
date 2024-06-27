package com.example.storyapp.ui.story.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.utils.getImage
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private var currentImage: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncherLocation = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLocation()
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLocation()
            }

            else -> {
                // getMyLocation()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

        binding.materialSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                getMyLocation()
            } else {
                lat = null
                lon = null
            }
        }

        supportActionBar?.title = getString(R.string.add_story_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    Toast.makeText(
                        this@AddStory, R.string.data_failed, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncherLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImage = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImage = getImage(this)
        currentImage?.let { uri ->
            launcherCamera.launch(uri)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImage?.let {
            Log.d("Image URI", "Show image: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun uploadImage() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        addStoryViewModel = ViewModelProvider(this, factory).get(AddStoryViewModel::class.java)

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        addStoryViewModel.isError.observe(this) { isError ->
            if (isError) {
                addStoryViewModel.errorMessage.observe(this) { errorMessage ->
                    if (errorMessage != null) {
                        showToast(errorMessage)
                        Log.d("Upload", errorMessage)
                    }
                }
            } else {
                showToast(getString(R.string.success_image))

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        currentImage?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edtDescription.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestLat = lat?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestLong = lon?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.addStory(multipartBody, requestBody, requestLat, requestLong)

        } ?: showToast(getString(R.string.empty_image))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}