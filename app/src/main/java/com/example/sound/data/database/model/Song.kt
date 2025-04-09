package com.example.sound.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [Index(value = ["songUri"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songUri: String,
    val albumId: Long? = null,
    val name: String,
    val imageUri: String? = null,
    val artist: String? = "Unknown artist",
//    val published: OffsetDateTime? = null,
    val duration: Long,
)
