package com.example.memolane.viewmodel

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> get() = _journals

    val journalListUiState: StateFlow<JournalUiState> =
        journalRepository.getJournals().map { JournalUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = JournalUiState()
            )

    companion object { private const val TIMEOUT_MILLIS = 5_000L }

    fun saveJournal(journal: Journal) {
        viewModelScope.launch {
            journalRepository.saveJournal(journal)
        }
    }
}
