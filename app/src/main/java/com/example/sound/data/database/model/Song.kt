package com.example.sound.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [Index(value = ["songUri"], unique = true)],
)
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long = 0,
    val songUri: String,
    val name: String,
    val albumId: Long? = null,
    val artist: String? = "Unknown artist",
    val duration: Long,
)
