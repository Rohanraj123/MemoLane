package com.example.memolane.util

import android.app.Activity
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

private const val STORAGE_PERMISSION_CODE = 1

object PermissionUtil {
    fun requestStoragePermission(activity: Activity): Boolean {
        val rationale = "We need storage permission to access files on your device"

        if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(activity)
                    .setTitle("Storage permission needed")
                    .setMessage(rationale)
                    .setPositiveButton("OK") { _, _ ->
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
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE)
            }
            return false
        }
    }
}
