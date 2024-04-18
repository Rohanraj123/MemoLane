package com.example.memolane.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import com.example.memolane.data.Journal
import com.example.memolane.data.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val journalRepository: JournalRepository
): ViewModel() {

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> get() = _journals

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> get() = _permissionGranted

    val journaListUiState: StateFlow<JournalUiState> =
        journalRepository.getJournals().map { JournalUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = JournalUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    init {
        loadJournals()
    }

    private fun loadJournals() {
        viewModelScope.launch {
            journalRepository.getJournals().collect {journals ->
                _journals.value = journals
            }
        }
    }

    fun deleteJournal(journalId: Long) {
        viewModelScope.launch {
            journalRepository.deleteJournal(journalId)
        }
    }

    suspend fun editJournal(journal: Journal) {
        Log.d("ViewModel", "editJournal method called successfully with updated Journal: $journal")
        journalRepository.editJournal(journal)
        // Update the specific  journal in state flow
        val index = _journals.value.indexOfFirst { it.id == journal.id }
        if(index != -1) {
            val updatedJournals = _journals.value.toMutableList()
            updatedJournals[index] = journal
            _journals.value = updatedJournals
        }
    }

    suspend fun getJournalById(journalId: Long): Journal {
        return journalRepository.getJournalById(journalId)
    }

    suspend fun readJournal(journalId: Long): String {
        return journalRepository.readJournal(journalId)
    }

    fun saveJournal(journal: Journal) {
        viewModelScope.launch {
            journalRepository.saveJournal(journal)
        }
    }
}

data class JournalUiState(val journalList: List<Journal> = listOf())