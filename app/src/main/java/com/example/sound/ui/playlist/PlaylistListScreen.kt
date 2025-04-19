package com.example.sound.ui.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs

@Composable
fun PlaylistListScreen(
    onPlaylistClicked: (Playlist) -> Unit,
    onCreatePlaylistButtonClicked: () -> Unit,
    viewModel: PlaylistListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        Button(
            onClick = onCreatePlaylistButtonClicked
        ) {
            Text(text = "Create playlist")
        }
        LazyColumn {
            items(uiState.playlists) { playlist ->
                PlaylistContainer(
                    playlistWithSongs = playlist,
                    onPlaylistClicked = onPlaylistClicked
                )
            }
        }
    }
}

@Composable
fun PlaylistContainer(
    onPlaylistClicked: (Playlist) -> Unit,
    playlistWithSongs: PlaylistWithSongs
) {
    Column(
        modifier = Modifier
            .clickable {
                onPlaylistClicked(playlistWithSongs.playlist)
            }
    ) {
        Text(text = playlistWithSongs.playlist.name)
    }
}