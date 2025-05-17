package com.example.sound.ui.home

import android.annotation.SuppressLint
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import com.example.sound.ui.shared.isAlbumArtAvailable

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

    val trimmedQuery = queryText.trim()
    val songList = songs.filter {
        it.name.contains(trimmedQuery, ignoreCase = true) || it.artist?.contains(
            trimmedQuery,
            ignoreCase = true
        ) == true
    }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Music",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search for songs") },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (queryText.isNotEmpty()) {
                            IconButton(onClick = { queryText = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            authState = authState,
            songContainerList = uiState.songContainers,
            onSongClick = { selectedSong ->
                Log.d("AlbumID", "Clicked song albumId = ${selectedSong.albumId}")
                playerViewModel.setCustomPlaylist(songList, selectedSong)
                onSongClick(selectedSong)
            },
            viewModel = viewModel,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            queryText = queryText.trim()
        )
    }
}

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
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.switchSource(SongSourceType.LOCAL)
                    },
                    enabled = currentSource != SongSourceType.LOCAL,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentSource == SongSourceType.LOCAL)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        "Local Songs",
                        color = if (currentSource == SongSourceType.LOCAL)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = {
                        viewModel.switchSource(SongSourceType.REMOTE)
                        viewModel.loadRemoteSongs()
                    },
                    enabled = currentSource != SongSourceType.REMOTE,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentSource == SongSourceType.REMOTE)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        "Remote Songs",
                        color = if (currentSource == SongSourceType.REMOTE)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Refresh Button
        OutlinedButton(
            onClick = viewModel::refreshSongs,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Refresh songs")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Song List
        if (filteredSongContainers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (queryText.isNotEmpty()) "No songs found" else "No songs available",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredSongContainers) { songContainer ->
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
    onSongShareClick: (Song, File, File) -> Unit = { _, _, _ -> },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasAlbumArt = remember(song.albumId) {
        mutableStateOf(false)
    }

    LaunchedEffect(song.albumId) {
        song.albumId?.let {
            hasAlbumArt.value = isAlbumArtAvailable(context, it)
        }
    }

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
            val albumArtUri = if (hasAlbumArt.value && song.albumId != null) {
                ContentUris.withAppendedId(
                    "content://media/external/audio/albumart".toUri(),
                    song.albumId
                )
            } else {
                "android.resource://${context.packageName}/${R.drawable.faker1}".toUri()
            }


            AsyncImage(
                model = albumArtUri,
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
                        val songFile = File(context.cacheDir, "${song.name}.mp3")
                        songFile.createNewFile()
                        context.contentResolver.openInputStream(song.songUri.toUri())
                            ?.use { inputStream ->
                                songFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            ?: throw IllegalArgumentException("Cannot open input stream from: ${song.songUri}")

                        val albumArtFile = File(context.cacheDir, "albumArt.jpg")
                        if (hasAlbumArt.value && song.albumId != null) {
                            // Trường hợp có album art từ MediaStore
                            context.contentResolver.openInputStream(albumArtUri)
                                ?.use { inputStream ->
                                    albumArtFile.outputStream().use { outputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                        } else {
                            // Trường hợp không có album art → dùng ảnh mặc định từ drawable
                            val bitmap =
                                BitmapFactory.decodeResource(context.resources, R.drawable.faker1)
                            albumArtFile.outputStream().use { outputStream ->
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            }
                        }

                        onSongShareClick(song, songFile, albumArtFile)
                    }, modifier = Modifier.size(40.dp)
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
                            Toast.makeText(
                                context, songUploadUiState.song.songUri, Toast.LENGTH_LONG
                            ).show()
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
        onSongShareClick = { _, _, _ ->

        },
    )
}
