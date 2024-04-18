package com.example.memolane.data

import androidx.annotation.RequiresPermission
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    /* All the methods to perform CRUD op. */
    /* Save the journal */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveJournal(journal: Journal)

    /* Delete the journal */
    @Query("DELETE FROM journals WHERE id = :journalId")
    suspend fun deleteJournal(journalId: Long)

    /* Edit the journal */
    @Update
    suspend fun editJournal(journal: Journal)

    /* Read the journal */
    @Query("SELECT content FROM journals WHERE id=:journalId")
    suspend fun readJournal(journalId: Long): String

    /* Get all the journals */
    @Query("SELECT * FROM journals")
    fun getJournals(): Flow<List<Journal>>

    /* Get one journal by ID */
    @Query("SELECT * FROM journals WHERE id = :journalId")
    suspend fun getJournalById(journalId: Long): Journal

    /* Get background image data by Journal ID */
    @Query("SELECT backgroundImage FROM journals WHERE id = :journalId")
    suspend fun getBackgroundImage(journalId: Long): ByteArray?

    /* Update background image data by journal ID */
    @Query("UPDATE journals SET backgroundImage = :imageData WHERE id = :journalId")
    suspend fun updateBackgroundImage(journalId: Long, imageData: ByteArray)
}