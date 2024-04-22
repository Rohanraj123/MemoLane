package com.example.memolane.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageSelectionViewModel: ViewModel() {
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?>  = _selectedImageUri

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }
}