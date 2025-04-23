package com.example.sound.ui.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import com.example.sound.data.database.model.Song
import com.example.sound.ui.home.SongContainer
import com.example.sound.ui.player.PlayerViewModel

@Composable
fun PlaylistDetailScreen(
    onSongClick: (Song) -> Unit,
    viewModel: PlaylistDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        Text(
            text = uiState.playlist.name
        )
        LazyColumn {
            items(uiState.songs) { song ->
                // TODO change to different component or make SongContainer shared maybe idk...
                SongContainer(
                    song = song,
                    onSongClick = {
                        playerViewModel.setCustomPlaylist(uiState.songs, song)
                        onSongClick(song)
                    }
                )
            }
        }
    }
}