package com.example.sound.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sound.SoundApplication
import com.example.sound.data.repository.TokenRepository
import com.example.sound.ui.album.AlbumDetailScreen
import com.example.sound.ui.album.AlbumListScreen
import com.example.sound.ui.home.HomeScreen
import com.example.sound.ui.loginPage.LoginScreen
import com.example.sound.ui.loginPage.SignupScreen
import com.example.sound.ui.loginPage.authService.AuthViewModel
import com.example.sound.ui.loginPage.authService.AuthViewModelFactory
import com.example.sound.ui.player.PlayerScreen
import com.example.sound.ui.player.PlayerViewModel
import com.example.sound.ui.playlist.PlaylistDetailScreen
import com.example.sound.ui.playlist.PlaylistEntryScreen
import com.example.sound.ui.playlist.PlaylistListScreen
import com.example.sound.ui.shared.MiniPlayer
import com.example.sound.ui.shared.MyBottomBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.sound.ui.home.HomeViewModel
import com.example.sound.ui.shared.WaitingScreen
import com.example.sound.ui.user.LibraryScreen
import com.example.sound.ui.user.SettingScreen
import kotlin.toString


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

    object Login : Screen("login")
    object Signup : Screen("register")
    object Account : Screen("account")

    companion object {
        const val ARG_SONG_URI = "songUri"
        const val ARG_ALBUM_NAME = "albumName"
        const val ARG_PLAYLIST_ID = "playlistId"
    }

    object Settings : Screen("settings_route")

}


const val TAG = "SoundApp"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SoundApp(
    isDarkMode: Boolean,
    onDarkModeClicked: () -> Unit,
) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
    val authState = authViewModel.authState.collectAsState()
    val username by authViewModel.username.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val mediaControllerReady by SoundApplication.mediaControllerReady.collectAsState(false)

    if (!mediaControllerReady) {
        WaitingScreen()
        return
    }

    // ✅ mediaController đã sẵn sàng, tạo ViewModel
    val playerViewModel: PlayerViewModel = viewModel(
        viewModelStoreOwner = context as ComponentActivity,
        factory = AppViewModelProvider.Factory
    )

    val showMiniPlayer = currentRoute != Screen.Player.route

    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Player.route) {
                Column {
                    if (showMiniPlayer) {
                        MiniPlayer(
                            playerViewModel = playerViewModel,
                            onExpand = { navController.navigate(Screen.Player.route) }
                        )
                    }
                    MyBottomBar(navController)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginButtonClicked = { username, password ->

                    },
                    onSignupRouteClicked = {
                        navController.navigate(Screen.Signup.route)
                    },
                    onLoginSuccess = { token ->
//                        tokenManager.putToken(token)
                        navController.navigate(Screen.Account.route)
//                        Log.i("SoundApp", tokenManager.getToken() ?: "no token lol")
                    }
                )
            }
            composable(route = Screen.Account.route) {
                LibraryScreen(
                    username = username ?: "Anonymous User",
                    isDarkMode = isDarkMode,
                    onDarkModeClicked = onDarkModeClicked,
                    onLogoutButtonClicked = {
                        authViewModel.logout()
                        Log.i("SoundApp", authState.toString())
                        navController.navigate(Screen.Login.route)
                    },
                    onUserUnauthenticated = {
                        navController.navigate(Screen.Login.route)
                    },
                    authState = authState.value,
                    onSongClick = {},
                    playerViewModel = playerViewModel,
                    viewModel = homeViewModel,
                    onSettingsClicked = { navController.navigate("settings_route") },
                )
            }
            composable(route = Screen.Signup.route) {
                SignupScreen(
                    authViewModel = authViewModel,
                    onLoginRouteClicked = {
                        navController.navigate(Screen.Login.route)
                    },
                    onRegisterButtonClicked = { username, email, password ->

                    },
                    onSignupSuccess = { token ->
//                        tokenManager.putToken(token)
                        navController.navigate(Screen.Home.route)
//                        Log.i("SoundApp", tokenManager.getToken() ?: "no token lol")
                    }
                )
            }
            composable(route = Screen.Home.route) {
                HomeScreen(
                    authState = authState.value,
                    onSongClick = {},
                    playerViewModel = playerViewModel,
                    viewModel = homeViewModel
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
                    },
                    onBackClick = { navController.popBackStack() }
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
//                        navController.navigate(Screen.Player.route)
                    },
                    onPlaylistDeleted = {
                        navController.navigate(Screen.PlaylistList.route) {
                            popUpTo(Screen.PlaylistList.route) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(route = Screen.AlbumList.route) {
                AlbumListScreen(
                    onAlbumClick = { album ->
                        navController.navigate(Screen.AlbumDetail.createRoute(album))
                    },
                    homeViewModel = homeViewModel,
                    playerViewModel = playerViewModel
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
                    playerViewModel = playerViewModel,
                    onSongClick = { song ->
//                        navController.navigate(Screen.Player.route)
                    },
                    onBackClick = { navController.popBackStack() }

                )
            }
            composable(route = Screen.Player.route) {
                PlayerScreen(
                    playerViewModel = playerViewModel,
                    onBackClick = { navController.popBackStack() })
            }
            composable(route = Screen.Settings.route) {
                SettingScreen(
                    onNavigateToAccountSettings = {},
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}