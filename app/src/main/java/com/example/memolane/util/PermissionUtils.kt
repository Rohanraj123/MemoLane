package com.example.memolane.util

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

object PermissionUtils {

    private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 123

    @Composable
    fun RequestStoragePermission(activity: Activity, onPermissionResult: (Boolean) -> Unit) {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted: Boolean ->
            onPermissionResult(isGranted)
        }

        if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            onPermissionResult(true) // Permission already granted
        }
    }
}