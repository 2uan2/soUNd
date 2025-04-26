package com.example.sound

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.sound.ui.loginPage.LoginScreen
import com.example.sound.ui.loginPage.SignupScreen
import com.example.sound.ui.theme.SoUNdTheme
import com.example.sound.ui.SoundApp

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
            SoUNdTheme {
                SoundApp()
//                LoginScreen()
//                SignupScreen()
            }

        }
    }
}