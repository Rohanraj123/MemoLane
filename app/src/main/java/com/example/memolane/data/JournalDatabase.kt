package com.example.memolane.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Journal::class], version=1, exportSchema=false)
abstract class JournalDatabase:RoomDatabase() {
    abstract val journalDao: JournalDao

}