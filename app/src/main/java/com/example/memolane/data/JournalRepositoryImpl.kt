package com.example.memolane.data

import kotlinx.coroutines.flow.Flow

class JournalRepositoryImpl(
    private val journalDao: JournalDao
): JournalRepository {
    override suspend fun saveJournal(journal: Journal) {
        journalDao.saveJournal(journal)
    }

    override suspend fun deleteJournal(journalId: Long) {
        journalDao.deleteJournal(journalId)
    }

    override suspend fun editJournal(journal: Journal) {
        journalDao.editJournal(journal)
    }

    override suspend fun readJournal(journalId: Long): String {
        return journalDao.readJournal(journalId)
    }

    override fun getJournals(): Flow<List<Journal>> {
        return journalDao.getJournals()
    }

    override suspend fun getJournalById(journalId: Long): Journal {
        return journalDao.getJournalById(journalId)
    }
}
