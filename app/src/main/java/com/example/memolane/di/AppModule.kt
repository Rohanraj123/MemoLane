package com.example.memolane.di

import android.app.Application
import androidx.room.Room
import com.example.memolane.data.JournalDao
import com.example.memolane.data.JournalDatabase
import com.example.memolane.data.JournalRepository
import com.example.memolane.data.JournalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesJournalDatabase(application: Application): JournalDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            JournalDatabase::class.java,
            "journal_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesJournalDao(journalDatabase: JournalDatabase): JournalDao {
        return journalDatabase.journalDao
    }

    @Provides
    @Singleton
    fun providesJournalRepository(journalDao: JournalDao): JournalRepository {
        return JournalRepositoryImpl(journalDao)
    }
}