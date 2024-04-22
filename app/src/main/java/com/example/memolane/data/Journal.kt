package com.example.memolane.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class Journal(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val dateTime: Long,
    var content: String,
    val backgroundImageUrl: String?,
    val soundTrackUrl: String?
)
