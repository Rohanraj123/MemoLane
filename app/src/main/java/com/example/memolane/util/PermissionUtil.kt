package com.example.memolane.util

import android.app.Activity
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

private const val STORAGE_PERMISSION_CODE = 1


object PermissionUtil {
    fun requestStoragePermission(activity: Activity): Boolean {
        val rationale = "We need storage permission to access files on your device"

        // Check whether the permission is already granted
        if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            return true
        } else {
            // Permission is not granted yet
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show rationale if needed
                AlertDialog.Builder(activity)
                    .setTitle("Storage permission needed")
                    .setMessage(rationale)
                    .setPositiveButton("OK") { _, _ ->
                        // Request permission
                        ActivityCompat.requestPermissions(activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                // Request permission directly
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE)
            }
            // Permission not granted yet
            return false
        }
    }
}

/*
* First of all clicked on image button
* it should ask for permission - > askPermission()
* if denied then stop the process else continue
* open the gallery
* select the item
* take that item and put that in place of default image
* if no image selected
* then use the default one
* */

/* work done yet
* --> Check for permission already granted or not
* --> If not granted permission
* --> Granted permission */


// You must check whether you have permission every time you perform the operation
// Request the run time permission that your user needs to give
// Check the users response whether denied or granted the permission
// if granted (access the data) else degrade app experience and use the default photo in the card view