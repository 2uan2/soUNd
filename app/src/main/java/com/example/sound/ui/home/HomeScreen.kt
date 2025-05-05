package com.example.sound.ui.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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

    val songList = songs.filter {
        it.name.contains(queryText, ignoreCase = true) ||
                it.artist?.contains(queryText, ignoreCase = true) == true
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier = modifier.padding(16.dp),
                query = queryText,
                onQueryChange = { queryText = it },
                onSearch = { },
                active = false,
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
            ) {}
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClick(song) }
    ) {
        val albumId = song.albumId
        val albumArtUri = if (albumId != null) {
            ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
        } else null
        AsyncImage(
            model = albumArtUri ?: R.drawable.faker1,
            contentDescription = null,
            modifier = modifier
                .size(48.dp)
        )
        Column(
            modifier = modifier
        ) {
            Text(
                text = song.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
            Text(text = song.artist ?: "Unknown artist")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = formatTime(song.duration))

        // if user is authenticated, then display button to share song
        if (authState == AuthState.Authenticated) {
            Button(
                onClick = {
                    val albumId = song.albumId
                    val albumArtUri = if (albumId != null) {
                        ContentUris.withAppendedId(
                            Uri.parse("content://media/external/audio/albumart"),
                            albumId
                        )
                    } else Uri.parse("android.resource://${context.packageName}/R.drawable.faker1")

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
                }
            ) {
                when (songUploadUiState) {
                    is SongUploadUiState.Error -> {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null
                        )
                    }
                    SongUploadUiState.Idle -> {
                        Icon(
                            Icons.Default.ArrowUpward,
                            contentDescription = null
                        )
                    }
                    SongUploadUiState.Loading -> {
                        Text(text = "Loading...")
                    }
                    is SongUploadUiState.Success -> {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null
                        )
                        Toast.makeText(context, songUploadUiState.song.songUri, Toast.LENGTH_LONG)
                            .show()
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
