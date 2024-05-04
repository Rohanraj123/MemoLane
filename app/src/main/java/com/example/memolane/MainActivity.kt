package com.example.memolane

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.memolane.ui.theme.MemoLaneTheme
import com.example.memolane.view.navigation.Navigation
import com.example.memolane.viewmodel.ImageSelectionViewModel
import com.example.memolane.viewmodel.MyViewModel
import com.example.memolane.viewmodel.NewJournalEditScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imageSelectionViewModel: ImageSelectionViewModel by viewModels()

    private var imageUri by mutableStateOf<Uri?>(null)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                takePicture()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryResult =
            registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                imageSelectionViewModel.setSelectedImageUri(it)
            }
        }
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    saveImageToGallery(uri)
                    imageSelectionViewModel.setSelectedImageUri(uri)
                    Log.d("CameraActivity", "selectedImageUri : $uri")
                    setResult(RESULT_OK, Intent().apply {
                        data = uri
                    })
                } ?: run {
                    setResult(RESULT_CANCELED)
                }
            } else {
                Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT).show()
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoLaneTheme {
                val myViewModel = hiltViewModel<MyViewModel>()
                val newJournalEditScreenViewModel = hiltViewModel<NewJournalEditScreenViewModel>()
                val navController = rememberNavController()

                val imageSelectionViewModel =
                    ViewModelProvider(this)[ImageSelectionViewModel::class.java]

                fun onCameraButtonClicked() {
                    checkCameraPermission()
                }

                Navigation(
                    navController = navController,
                    myViewModel,
                    newJournalEditScreenViewModel,
                    activity = this@MainActivity,
                    onImageButtonClicked = {openGalleryForImage()},
                    imageSelectionViewModel,
                    { onCameraButtonClicked() }
                )
            }
        }
    }
    @SuppressLint("IntentReset")
    private fun openGalleryForImage() {
        val imageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageIntent.type = "image/*"
        galleryResult.launch(imageIntent)
    }

    private fun takePicture() {
        imageUri?.let { cameraResult.launch(it) }
    }

    private fun saveImageToGallery(imageUri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/MemoLane")
            }
        }
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?.let { outputStream ->
                contentResolver.openOutputStream(outputStream).use { output ->
                    contentResolver.openInputStream(imageUri)?.use { input ->
                        if (output != null) {
                            input.copyTo(output)
                        }
                    }
                }
            }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun startCamera() {
        val photoUri = createImageUri()
        photoUri?.let {
            imageUri = it
            cameraResult.launch(it)
        }
    }
    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/MemoLane")
            }
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}
