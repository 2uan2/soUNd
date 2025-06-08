package com.example.sound.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sound.data.database.model.Song
import com.example.sound.ui.loginPage.authService.AuthState
import com.example.sound.ui.shared.EmptySongListUI
import com.example.sound.ui.shared.RemoteSongContainer
import com.example.sound.ui.shared.SongContainer

@Composable
fun HomeBody(
    authState: AuthState,
    songContainerList: List<SongContainerUiState>,
    onSongClick: (Song) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier,
    queryText: String
) {
    val currentSource by viewModel.currentSource.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val filteredSongContainers = songContainerList.filter {
        it.song.name.contains(queryText, ignoreCase = true) ||
                it.song.artist?.contains(queryText, ignoreCase = true) == true
    }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.song.name })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Source Selection Buttons
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.switchSource(SongSourceType.LOCAL) },
                    enabled = currentSource != SongSourceType.LOCAL,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .widthIn(min = 120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentSource == SongSourceType.LOCAL)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = if (currentSource == SongSourceType.LOCAL)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "Local",
                        fontWeight = if (currentSource == SongSourceType.LOCAL) FontWeight.Bold else FontWeight.Normal
                    )
                }

                IconButton(
                    onClick = viewModel::refreshSongs,
                    enabled = !isLoading && !isRefreshing,
                    modifier = Modifier.size(40.dp) // Adjust size as needed
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.switchSource(SongSourceType.REMOTE)
                        viewModel.loadRemoteSongs()
                    },
                    enabled = currentSource != SongSourceType.REMOTE,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .widthIn(min = 120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentSource == SongSourceType.REMOTE)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = if (currentSource == SongSourceType.REMOTE)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "Remote",
                        fontWeight = if (currentSource == SongSourceType.REMOTE) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Refresh Button
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedButton(
//                onClick = viewModel::refreshSongs,
//                enabled = !isLoading && !isRefreshing,
//                colors = ButtonDefaults.outlinedButtonColors(
//                    contentColor = MaterialTheme.colorScheme.primary,
//                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
//                )
//            ) {
//                if (isRefreshing) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(18.dp),
//                        strokeWidth = 2.dp,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Refreshing...")
//                } else {
//                    Icon(
//                        Icons.Default.Refresh,
//                        contentDescription = "Refresh",
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Refresh songs")
//                }
//            }
//        }


        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        // Song List
        if (filteredSongContainers.isEmpty()) {
            EmptySongListUI(queryText = queryText)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredSongContainers) { songContainer ->

                    when (currentSource) {
                        SongSourceType.LOCAL -> {
                            SongContainer(
                                authState = authState,
                                song = songContainer.song,
                                songUploadUiState = songContainer.songUploadState,
                                onSongClick = onSongClick,
                                onSongShareClick = { song, songFile, albumArtFile ->
                                    viewModel.uploadSong(song, songFile, albumArtFile)
                                }
                            )
                        }

                        SongSourceType.REMOTE -> {
                            RemoteSongContainer(
                                authState = authState,
                                song = songContainer.song,
                                isFavourite = songContainer.song.isFavourited, // truyền trạng thái yêu thích
                                onFavouriteToggle = { viewModel.onFavouriteToggle(it.serverId) }, //  truyền callback
                                onSongClick = onSongClick
                            )
                        }
                    }
                }
            }
        }
    }
}
