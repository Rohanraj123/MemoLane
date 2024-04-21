package com.example.memolane.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData

object ImageUtil {
    const val IMAGE_PICK_CODE = 1000

    fun openGalleryForImage(activity: Activity) {
        val imageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageIntent.type = "image/*"
        activity.startActivityForResult(imageIntent, IMAGE_PICK_CODE)
    }

    fun getImageFromActivityResult(requestCode: Int, resultCode: Int, data: Intent?): String? {
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            return selectedImageUri?.toString()
        }
        return null
    }
}