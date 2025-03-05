package com.example.submissionstoryapp.presentation.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.example.submissionstoryapp.presentation.MainViewModel
import com.example.submissionstoryapp.presentation.base.UiState
import com.example.submissionstoryapp.presentation.home.HomeActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: MainViewModel by viewModels()
    private var currentPhotoPath: String? = null
    private var selectedImageUri: Uri? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.apply {
            cameraButton.setOnClickListener {
                checkPermission(
                    Manifest.permission.CAMERA,
                    ::openCamera
                )
            }
            galleryButton.setOnClickListener { checkGalleryPermission() }
            uploadButton.setOnClickListener {
                uploadButton.setUpload(true)
                checkPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ::getCurrentLocation
                )
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addStories.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            binding.uploadButton.setUpload(false)
                            navigateBack()

                        }

                        is UiState.Error -> {
                            binding.uploadButton.setUpload(false)
                            showToast(state.message)
                        }

                        is UiState.Loading -> binding.uploadButton.setUpload(true)
                        is UiState.Idle -> binding.uploadButton.setUpload(false)
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun checkPermission(permission: String, onGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> onGranted()

            else -> requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            showToast(if (isGranted) "Izin diberikan" else "Izin Ditolak")
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri = Uri.fromFile(File(currentPhotoPath!!)).also {
                    binding.previewImageView.setImageURI(it)
                }
            }
        }

    private fun openCamera() {
        createImageFile()?.let { file ->
            val photoUri: Uri = FileProvider.getUriForFile(this, "$packageName.fileProvider", file)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                cameraLauncher.launch(this)
            }
        } ?: showToast("Gagal membuat file gambar")
    }

    private fun checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(Manifest.permission.READ_MEDIA_IMAGES, ::openGallery)
        } else {
            openGallery()
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                selectedImageUri = it
                binding.previewImageView.setImageURI(it)
            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun createImageFile(): File? {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return if (storageDir?.exists() == true || storageDir?.mkdirs() == true) {
            File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", storageDir).apply {
                currentPhotoPath = absolutePath
            }
        } else null
    }

    private fun uriToFile(uri: Uri): File {
        val file = File(cacheDir, "temp_image.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file
    }

    private fun uploadStory(lat: Float?, lon: Float?) {
        val description = binding.descriptionEditText.text.toString().trim()
        if (description.isEmpty() || selectedImageUri == null) {
            showToast("Deskripsi dan gambar tidak boleh kosong")
            return
        }
        uriToFile(selectedImageUri!!).let {
            viewModel.addStoriesViewModel(description, it, lat, lon)
            showToast("Berhasil unggah cerita!")
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!isLocationEnabled()) {
            showToast("Harap aktifkan GPS untuk mendapatkan lokasi")
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Jika belum, minta izin
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //Jika ijin diberikan, ambil lokasi
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let { uploadStory(it.latitude.toFloat(), it.longitude.toFloat()) }
                    ?: showToast("Gagal mendapatkan lokasi")
            }
            .addOnFailureListener { showToast("Gagal mendapatkan lokasi: ${it.message}") }

    }

    // Tangani hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation() // Jika izin diberikan, panggil kembali fungsi
            } else {
                showToast("Izin lokasi ditolak")
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
