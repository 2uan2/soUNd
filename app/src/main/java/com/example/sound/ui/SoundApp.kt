package com.example.sound.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sound.ui.album.AlbumScreen
import com.example.sound.ui.home.HomeScreen
import com.example.sound.ui.player.PlayerScreen
import com.example.sound.ui.shared.MyBottomBar

sealed class Screen(val route:String) {
    object Home : Screen("home")
    object Album : Screen("album/{$ARG_ALBUM_URI}") {
        fun createRoute(albumUri: String) = "album/${Uri.encode(albumUri)}"
    }
    object Player : Screen("player/{$ARG_SONG_URI}") {
        fun createRoute(songUri: String) = "player/${Uri.encode(songUri)}"
    }

    companion object {
        val ARG_SONG_URI = "songUri"
        val ARG_ALBUM_URI = "albumUri"
    }
}

const val TAG = "SoundApp"
@Composable
fun SoundApp(

) {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    onSongClick = { song ->
                        Log.i(TAG, "${song.songUri}")
                        Log.i(TAG, Screen.Player.createRoute(song.songUri))
                        navController.navigate(Screen.Player.createRoute(song.songUri))
                    }
                )
            }
            composable(route = Screen.Album.route) {
                AlbumScreen(

                )
            }
            composable(
                route = Screen.Player.route,
                arguments = listOf(
                    navArgument(Screen.ARG_SONG_URI) {
                        type = NavType.StringType
                        defaultValue = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                    }
                )
            ) {
                Log.i(TAG, it.arguments?.getString(Screen.ARG_SONG_URI).toString())
                PlayerScreen(
                    songUri = it.arguments?.getString(Screen.ARG_SONG_URI).toString()
                )
            }
        }
    }
}