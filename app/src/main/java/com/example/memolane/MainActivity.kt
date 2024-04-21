package com.example.memolane

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                imageSelectionViewModel.setSelectedImageUri(it)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoLaneTheme {
                val myViewModel = hiltViewModel<MyViewModel>()
                val newJournalEditScreenViewModel = hiltViewModel<NewJournalEditScreenViewModel>()
                val navController = rememberNavController()

                val imageSelectionViewModel = ViewModelProvider(this)[ImageSelectionViewModel::class.java]

                Navigation(
                    navController = navController,
                    myViewModel,
                    newJournalEditScreenViewModel,
                    activity = this@MainActivity,
                    onImageButtonClicked = {openGalleryForImage()},
                    imageSelectionViewModel
                )
            }
        }
    }
    @SuppressLint("IntentReset")
    private fun openGalleryForImage() {
        val imageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageIntent.type = "image/*"
        getContent.launch(imageIntent)
    }
}



