package com.example.sound.musicService

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.*
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberMediaController(context: Context): MediaController? {
    var controller by remember { mutableStateOf<MediaController?>(null) }

    LaunchedEffect(Unit) {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        val mediaController = withContext(Dispatchers.IO) {
            MediaController.Builder(context, sessionToken).buildAsync().get()
        }
        controller = mediaController
    }

    DisposableEffect(Unit) {
        onDispose {
            controller?.release()
        }
    }

    return controller
}
