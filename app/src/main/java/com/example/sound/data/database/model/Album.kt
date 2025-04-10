package com.example.sound.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums"
)
data class Album(
    @PrimaryKey
    val id: Long = 0,
    val name: String,
)
