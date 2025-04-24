package com.example.sound.ui

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sound.ui.album.AlbumDetailScreen
import com.example.sound.ui.album.AlbumListScreen
import com.example.sound.ui.home.HomeScreen
import com.example.sound.ui.player.PlayerScreen
import com.example.sound.ui.player.PlayerViewModel
import com.example.sound.ui.playlist.PlaylistDetailScreen
import com.example.sound.ui.playlist.PlaylistEntryScreen
import com.example.sound.ui.playlist.PlaylistListScreen
import com.example.sound.ui.shared.MyBottomBar

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Player : Screen("player") {
        fun createRoute(songUri: String) = "player/${Uri.encode(songUri)}"
    }

    object AlbumList : Screen("album")
    object AlbumDetail : Screen("album/{$ARG_ALBUM_NAME}") {
        fun createRoute(albumName: String) = "album/${Uri.encode(albumName)}"
    }

    object PlaylistList : Screen("playlist")
    object PlaylistEntry : Screen("playlist_entry")
    object PlaylistDetail : Screen("playlist/{$ARG_PLAYLIST_ID}") {
        fun createRoute(playlistId: Long) = "playlist/$playlistId"
    }

    companion object {
        const val ARG_SONG_URI = "songUri"
        const val ARG_ALBUM_NAME = "albumName"
        const val ARG_PLAYLIST_ID = "playlistId"
    }
}

const val TAG = "SoundApp"

@Composable
fun SoundApp(

) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val playerViewModel: PlayerViewModel = viewModel(
        viewModelStoreOwner = context as ComponentActivity,
        factory = AppViewModelProvider.Factory
    )


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
                    onSongClick = {},
                    playerViewModel = playerViewModel
                )
            }
            composable(route = Screen.PlaylistList.route) {
                PlaylistListScreen(
                    onCreatePlaylistButtonClicked = {
                        navController.navigate(Screen.PlaylistEntry.route)
                    },
                    onPlaylistClicked = { playlist ->
                        Log.i(TAG, Screen.PlaylistDetail.createRoute(playlist.playlistId))
                        navController.navigate(Screen.PlaylistDetail.createRoute(playlist.playlistId))
                    }
                )
            }
            composable(route = Screen.PlaylistEntry.route) {
                PlaylistEntryScreen(
                    onCreateButtonClicked = {
                        navController.navigate(Screen.PlaylistList.route)
                    }
                )
            }
            composable(
                route = Screen.PlaylistDetail.route,
                arguments = listOf(
                    navArgument(Screen.ARG_PLAYLIST_ID) {
                        type = NavType.LongType
                        defaultValue = 0
                    }
                )
            ) {
                PlaylistDetailScreen(
                    playerViewModel = playerViewModel,
                    onSongClick = { song ->
                        navController.navigate(Screen.Player.route)
                    },
                    onPlaylistDeleted = {
                        navController.navigate(Screen.PlaylistList.route) {
                            popUpTo(Screen.PlaylistList.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = Screen.AlbumList.route) {
                AlbumListScreen(
                    onAlbumClick = { album ->
                        navController.navigate(Screen.AlbumDetail.createRoute(album))
                    },
                )
            }
            composable(
                route = Screen.AlbumDetail.route,
                arguments = listOf(
                    navArgument(Screen.ARG_ALBUM_NAME) {
                        type = NavType.StringType
                        defaultValue = "Unknown artist"
                    }
                )
            ) { backStackEntry ->
                AlbumDetailScreen(
                    onSongClick = { song ->
                        navController.navigate(Screen.Player.route)
                    }
                )
            }
            composable(route = Screen.Player.route) {
                PlayerScreen(playerViewModel = playerViewModel)
            }
        }
    }
}