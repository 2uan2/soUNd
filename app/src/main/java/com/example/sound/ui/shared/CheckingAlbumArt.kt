package com.example.sound.ui.shared

import android.content.ContentUris
import android.content.Context
import androidx.core.net.toUri

fun isAlbumArtAvailable(context: Context, albumId: Long): Boolean {
    val albumArtUri = ContentUris.withAppendedId(
        "content://media/external/audio/albumart".toUri(), albumId
    )

    return try {
        val inputStream = context.contentResolver.openInputStream(albumArtUri)
        inputStream?.close()
        inputStream != null
    } catch (e: Exception) {
        false
    }
}
