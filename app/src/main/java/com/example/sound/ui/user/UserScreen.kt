package com.example.sound.ui.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sound.data.database.model.Song
import com.example.sound.ui.home.HomeViewModel
import com.example.sound.ui.home.SongContainerUiState
import com.example.sound.ui.loginPage.authService.AuthState
import com.example.sound.ui.player.PlayerViewModel


@Composable
fun LibraryScreen(
    username: String,
    isDarkMode: Boolean,
    onDarkModeClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit,
    onUserUnauthenticated: () -> Unit,
    authState: AuthState = AuthState.Unauthenticated,
    onSongClick: (Song) -> Unit,
    playerViewModel: PlayerViewModel,
    viewModel: HomeViewModel,
    onSettingsClicked: () -> Unit,
) {
    Log.i("LibraryScreen", "authState is $authState")

    when (authState) {
        AuthState.Authenticated -> {
            LaunchedEffect(Unit) {
                viewModel.loadRemoteSongs()
            }

            val allSongs by viewModel.songs.collectAsState()
            val uiState by viewModel.uiState.collectAsState()
            val favouriteRemoteSongs =
                allSongs.filter { (it.serverId != -1) && it.isFavourited }
                    .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

            Scaffold(
                topBar = {
                    TopBar(
                        username = username,
                        onSettingsClicked = onSettingsClicked
                    ) // Use your custom TopBar (already styled)
                },
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Button(
                            onClick = onLogoutButtonClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "Log out",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Greeting
                    Text(
                        text = "Hello $username",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    // Theme switch row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = if (isDarkMode) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onDarkModeClicked() }
                        )
                    }

                    Button(
                        onClick = { /* Handle Premium upgrade click */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp) // Reduced height
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Star,
                                contentDescription = "Premium",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Nâng cấp Premium", // Updated text to Vietnamese
                                fontSize = 16.sp, // Slightly smaller font size
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Favorite Songs List
                    LibraryList(
                        authState = authState,
                        songContainerList = uiState.songContainers,
                        onSongClick = { selectedSong ->
                            Log.d("AlbumID", "Clicked song albumId = ${selectedSong.albumId}")
                            playerViewModel.setCustomPlaylist(favouriteRemoteSongs, selectedSong)
                            onSongClick(selectedSong)
                        },
                        viewModel = viewModel
                    )
                }
            }
        }

        AuthState.Unauthenticated -> onUserUnauthenticated()
    }
}
