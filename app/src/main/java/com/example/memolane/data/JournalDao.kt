package com.example.memolane.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveJournal(journal: Journal)

    @Query("DELETE FROM journals WHERE id = :journalId")
    suspend fun deleteJournal(journalId: Long)

    @Update
    suspend fun editJournal(journal: Journal)

    @Query("SELECT content FROM journals WHERE id=:journalId")
    suspend fun readJournal(journalId: Long): String

    @Query("SELECT * FROM journals")
    fun getJournals(): Flow<List<Journal>>

    @Query("SELECT * FROM journals WHERE id = :journalId")
    suspend fun getJournalById(journalId: Long): Journal
}
