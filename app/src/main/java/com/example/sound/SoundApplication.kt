package com.example.sound

import android.app.Application
import android.content.ComponentName
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.sound.data.database.AppContainer
import com.example.sound.data.database.AppDataContainer
import com.example.sound.musicService.MusicService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SoundApplication : Application() {
    companion object {
        lateinit var container: AppContainer
        var mediaController: MediaController? = null
            private set

        private val _mediaControllerReady = MutableStateFlow(false)
        val mediaControllerReady: StateFlow<Boolean> get() = _mediaControllerReady
    }

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        val sessionToken = SessionToken(this, ComponentName(this, MusicService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get()
                _mediaControllerReady.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
