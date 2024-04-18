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
    val backgroundImage: ByteArray?, // Url or filepath of that image
    val soundTrackUrl: String? // Url or file path of that image
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Journal

        if (id != other.id) return false
        if (dateTime != other.dateTime) return false
        if (content != other.content) return false
        if (backgroundImage != null) {
            if (other.backgroundImage == null) return false
            if (!backgroundImage.contentEquals(other.backgroundImage)) return false
        } else if (other.backgroundImage != null) return false
        if (soundTrackUrl != other.soundTrackUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (backgroundImage?.contentHashCode() ?: 0)
        result = 31 * result + (soundTrackUrl?.hashCode() ?: 0)
        return result
    }
}
