package com.example.sound.ui.home

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.sound.R
import com.example.sound.data.database.model.Song
import com.example.sound.ui.AppViewModelProvider
import com.example.sound.ui.player.PlayerViewModel
import com.example.sound.ui.player.formatTime
import java.io.File
import androidx.core.net.toUri
import com.example.sound.ui.loginPage.authService.AuthState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authState: AuthState,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    playerViewModel: PlayerViewModel = viewModel()
) {
    var queryText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val songs by viewModel.songs.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }

    val trimmedQuery = queryText.trim()
    val songList = songs.filter {
        it.name.contains(trimmedQuery, ignoreCase = true) ||
                it.artist?.contains(trimmedQuery, ignoreCase = true) == true
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier = modifier.padding(16.dp),
                query = queryText,
                onQueryChange = { queryText = it },
                onSearch = { isSearchActive = false },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text("Search for songs") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (queryText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { queryText = "" }
                        )
                    } else {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                    }
                }
            ) {
                LazyColumn {
                    items(songList) { song ->
                        Text(
                            text = song.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    playerViewModel.setCustomPlaylist(songList, song)
                                    onSongClick(song)
                                    queryText = song.name
                                    isSearchActive = false
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        HomeBody(
            authState = authState,
            songContainerList = uiState.songContainers,
            onSongClick = { selectedSong ->
                playerViewModel.setCustomPlaylist(songList, selectedSong)
                onSongClick(selectedSong)
            },
            viewModel = viewModel,
            songList = songList, // ✅ now you're passing the filtered list
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        )

    }
}

@Composable
fun HomeBody(
    authState: AuthState,
    songContainerList: List<SongContainerUiState>,
    onSongClick: (Song) -> Unit,
    viewModel: HomeViewModel,
    songList: List<Song>,
    modifier: Modifier,
) {
    val currentSource by viewModel.currentSource.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔘 Toggle between Local and Remote
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.switchSource(SongSourceType.LOCAL)
                },
                enabled = currentSource != SongSourceType.LOCAL
            ) {
                Text("Local Songs")
            }

            Button(
                onClick = {
                    viewModel.switchSource(SongSourceType.REMOTE)
                    viewModel.loadRemoteSongs() // load remote songs if needed
                },
                enabled = currentSource != SongSourceType.REMOTE
            ) {
                Text("Remote Songs")
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = viewModel::refreshSongs
        ) {
            Text(text = "Refresh songs")
        }
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(songContainerList) { songContainer ->
                SongContainer(
                    authState = authState,
                    song = songContainer.song,
                    songUploadUiState = songContainer.songUploadState,
                    onSongClick = onSongClick,
                    onSongShareClick = { song, songFile, albumArtFile ->
                        viewModel.uploadSong(song, songFile, albumArtFile)
                    },
                )
            }
        }
    }
}

@Composable
fun SongContainer(
    song: Song,
    authState: AuthState = AuthState.Unauthenticated,
    songUploadUiState: SongUploadUiState = SongUploadUiState.Idle,
    onSongClick: (Song) -> Unit,
    onSongShareClick: (Song, File, File) -> Unit = { song, songFile, albumArtFile -> },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClick(song) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Art
            val albumId = song.albumId
            val albumArtUri = if (albumId != null) {
                ContentUris.withAppendedId(
                    "content://media/external/audio/albumart".toUri(),
                    albumId
                )
            } else null
            AsyncImage(
                model = albumArtUri ?: R.drawable.faker1,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Song Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = song.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist ?: "Unknown artist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Duration
            Text(
                text = formatTime(song.duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Upload Button (if authenticated)
            if (authState == AuthState.Authenticated) {
                IconButton(
                    onClick = {
                        val albumId = song.albumId
                        val albumArtUri = if (albumId != null) {
                            ContentUris.withAppendedId(
                                "content://media/external/audio/albumart".toUri(),
                                albumId
                            )
                        } else "android.resource://${context.packageName}/R.drawable.faker1".toUri()

                        val songFile = File(context.cacheDir, "${song.name}.mp3")
                        songFile.createNewFile()
                        context.contentResolver.openInputStream(song.songUri.toUri())
                            ?.use { inputStream ->
                                songFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            ?: throw IllegalArgumentException("Cannot open input stream from: ${song.songUri}")

                        val albumArtMimeType = context.contentResolver.getType(albumArtUri)
                        val albumArtExtensionType =
                            MimeTypeMap.getSingleton().getExtensionFromMimeType(albumArtMimeType)
                        val albumArtFile =
                            File(context.cacheDir, "albumArt.${albumArtExtensionType ?: "bin"}")
                        albumArtFile.createNewFile()
                        context.contentResolver.openInputStream(albumArtUri)?.use { inputStream ->
                            albumArtFile.outputStream().use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        onSongShareClick(song, songFile, albumArtFile)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    when (songUploadUiState) {
                        is SongUploadUiState.Error -> {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Upload Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        SongUploadUiState.Idle -> {
                            Icon(
                                Icons.Default.ArrowUpward,
                                contentDescription = "Upload Song",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        SongUploadUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        is SongUploadUiState.Success -> {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = "Upload Success",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Toast.makeText(context, songUploadUiState.song.songUri, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SongContainer(
        Song(
            songUri = "testing",
            name = "song",
            duration = 100000,
        ),
        onSongClick = {},
        onSongShareClick = { song, songFile, albumArtFile ->

        },
    )
}
