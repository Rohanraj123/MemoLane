package com.example.memolane.data

import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    /* Basic CRUD Operations */
    suspend fun saveJournal(journal: Journal)
    suspend fun deleteJournal(journalId: Long)
    suspend fun editJournal(journal: Journal)
    suspend fun readJournal(journalId: Long): String
    fun getJournals(): Flow<List<Journal>>
    suspend fun getJournalById(journalId: Long): Journal

}