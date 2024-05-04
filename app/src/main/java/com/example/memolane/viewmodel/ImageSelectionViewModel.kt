package com.example.memolane.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageSelectionViewModel: ViewModel() {
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?>  = _selectedImageUri

    fun setSelectedImageUri(uri: Uri?) {
        viewModelScope.launch {
            _selectedImageUri.value = uri
            Log.d("ViewModel", "selectedUri: ${_selectedImageUri.value}")
        }
    }
}