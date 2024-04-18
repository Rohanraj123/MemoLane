package com.example.memolane.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.IOException

private const val REQUEST_SELECT_CODE = 123

object ImageUtils {

    @Throws(IOException::class)
    fun readImageFromStorage(filePath: String): ByteArray? {
        val imageFile = File(filePath)
        if (!imageFile.exists()) {
            return null // Handle the case where image does not exist
        }

        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(imageFile)
            return fileInputStream.readBytes()
        } finally {
            fileInputStream?.close()
        }
    }

    fun selectImage(activity: Activity, onSelectImageResult: (String?) -> Unit) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        activity.startActivityForResult(intent, REQUEST_SELECT_CODE)
    }

    fun handleImageSelectedResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?, onSelectImageResult: (String?) -> Unit) {
        if (requestCode == REQUEST_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            val selectedImagePath = selectedImageUri?.let { uri ->
                // Retrieve the path of the selected image using a content resolver
                val filePath = getImagePathFromUri(activity, uri)
                filePath
            }
            onSelectImageResult(selectedImagePath)
        } else {
            onSelectImageResult(null)
        }
    }

    private fun getImagePathFromUri(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val pathIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                return it.getString(pathIndex)
            }
        }
        return null
    }
}