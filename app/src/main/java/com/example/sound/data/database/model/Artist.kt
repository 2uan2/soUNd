package com.example.sound.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
)
data class Artist(
    @PrimaryKey(autoGenerate = true)
    val artistId: Long = 0,
    val name: String,
)
