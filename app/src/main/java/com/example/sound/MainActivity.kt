package com.example.sound

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.sound.ui.loginPage.LoginScreen
import com.example.sound.ui.loginPage.SignupScreen
import com.example.sound.ui.theme.SoUNdTheme
import com.example.sound.ui.SoundApp
import com.example.sound.ui.shared.TrendingArtistsSection
import com.example.sound.ui.shared.WaitingScreen
import com.example.sound.ui.user.LibraryScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
            0
        )
        enableEdgeToEdge()
        setContent {
            val systemDarkMode = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemDarkMode) }
            SoUNdTheme(
                darkTheme = isDarkMode
            ) {
                SoundApp(
                    isDarkMode = isDarkMode,
                    onDarkModeClicked = {
                        isDarkMode = !isDarkMode
                    }
                )
//                LoginScreen()
//                SignupScreen()
//                WaitingScreen()
//                LibraryScreen()
//                TrendingArtistsSection()

            }

        }
    }
}