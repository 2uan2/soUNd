package com.example.sound.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sound.ui.album.AlbumScreen
import com.example.sound.ui.home.MainScreen
import com.example.sound.ui.player.PlayerScreen
import com.example.sound.ui.shared.MyBottomBar

enum class Screen(val route:String) {
    MAIN("main"),
    ALBUM("album"),
    PLAYER("player"),

}

@Composable
fun SoundApp(

) {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.MAIN.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.MAIN.name) {
                MainScreen(

                )
            }
            composable(route = Screen.ALBUM.name) {
                AlbumScreen(

                )
            }
            composable(route = Screen.PLAYER.name) {
                PlayerScreen(

                )
            }
        }
    }
}