package com.example.memolane.viewmodel

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memolane.data.Journal
import com.example.memolane.data.JournalRepository
import com.example.memolane.util.ImageUtil
import com.example.memolane.util.PermissionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewJournalEditScreenViewModel @Inject constructor(
    private val journalRepository: JournalRepository
): ViewModel() {

    /* UI operations starts here */
    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> get() = _journals

    val journaListUiState: StateFlow<JournalUiState> =
        journalRepository.getJournals().map { JournalUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = JournalUiState()
            )

    companion object { private const val TIMEOUT_MILLIS = 5_000L }

    /*UI operations ends here */

    /* Database operations starts here */

    fun saveJournal(journal: Journal) {
        viewModelScope.launch {
            journalRepository.saveJournal(journal)
        }
    }

    /* Database operations end here*/

    /* Image Button Feature implementation methods */
    fun onImageButtonClicked(activity: Activity) {
        // Ask for the permission
        val isPermissionGranted = PermissionUtil.requestStoragePermission(activity)

        if (isPermissionGranted) {
            // Open the storage
            ImageUtil.openGalleryForImage(activity)
        } else {
            Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG).show()
            // Just stop the process
        }
        // If granted then open the storage
        // afterImageSelection()

    }

    fun afterImageSelection() {
        // close the file storage
        // move back to the new screen
        // add the image in the place of background image
        // after save button we will add it to the database
    }
}