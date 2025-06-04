package com.example.sound.ui.user

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
    viewModel: HomeViewModel

) {
    Log.i("LibraryScreen", "authState is $authState")

    when (authState) {
        AuthState.Authenticated -> {
            LaunchedEffect(Unit) {
                viewModel.loadRemoteSongs()
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hello $username",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { checked ->
                                onDarkModeClicked()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDarkMode) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkMode) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black.copy(alpha = 0.8f)
                        )
                    }

                    Button(
                        onClick = onLogoutButtonClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(24),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Log out",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }

                    TopBar()
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryTabs()
                    Spacer(modifier = Modifier.height(12.dp))

                    val allSongs by viewModel.songs.collectAsState()
                    val uiState by viewModel.uiState.collectAsState()
                    val favouriteRemoteSongs =
                        allSongs.filter { (it.serverId != -1) && it.isFavourited }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
                    LibraryList(
                        authState = authState,
                        songContainerList = uiState.songContainers,
                        onSongClick = { selectedSong ->
                            Log.d("AlbumID", "Clicked song albumId = ${selectedSong.albumId}")
                            playerViewModel.setCustomPlaylist(favouriteRemoteSongs, selectedSong)
                            onSongClick(selectedSong)
                        },
                        viewModel = viewModel,


                    )
                }
            }
        }

        AuthState.Unauthenticated -> {
            onUserUnauthenticated()
        }
    }
}
