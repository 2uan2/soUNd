package com.example.sound

import android.app.Application
import com.example.sound.data.database.AppContainer
import com.example.sound.data.database.AppDataContainer

class SoundApplication : Application() {
    companion object {
        lateinit var container: AppContainer
    }

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}