package com.example.memolane.data

import androidx.compose.runtime.MutableState
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class Journal(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val dateTime: Long,
    var content: String,
    val backgroundImageUrl: String?, // Url or filepath of that image
    val soundTrackUrl: String? // Url or file path of that image
)
