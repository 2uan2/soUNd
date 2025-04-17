package com.example.sound.data.database.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["playlistId", "songId"],
    indices = [
        Index(value = ["playlistId"]),
        Index(value = ["songId"])
    ]

)
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long,
)