package com.example.sound.ui.playlist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.sound.ui.home.SongContainer

//const val TAG = "PlaylistEntryScreen"
@Composable
fun PlaylistEntryScreen(
    onCreateButtonClicked: () -> Unit,
    viewModel: PlaylistEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var playlistName by remember { mutableStateOf(uiState.playlistName) }
    var songSelections by remember { mutableStateOf(uiState.songSelections) }
    val isNameEmpty = playlistName.isBlank()
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.songSelections) {
        songSelections = uiState.songSelections
    }

    Column {
        Button(
            onClick = {
                if (playlistName.isNotBlank()) {
                    viewModel.setPlaylistName(playlistName)
                    viewModel.setSongSelection(songSelections)
                    viewModel.createPlaylist()
                    onCreateButtonClicked()
                } else {
                    showError = true
                }
            },
            enabled = !isNameEmpty
        ) {
            Text(text = "Create playlist")
        }

        TextField(
            value = playlistName,
            onValueChange = {
                playlistName = it
                showError = false
            },
            label = { Text("Playlist Name") },
            isError = showError
        )

        if (showError) {
            Text(
                text = "Playlist name cannot be empty",
                color = androidx.compose.ui.graphics.Color.Red
            )
        }

        LazyColumn {
            Log.i(TAG, songSelections.toString())
            items(songSelections) { songSelection ->
                SongSelectionContainer(
                    songSelection = songSelection,
                    onSelectionChanged = { changedSongSelection ->
                        songSelections = songSelections.map { songSelection ->
                            if (changedSongSelection.song.songId == songSelection.song.songId) {
                                songSelection.copy(selected = !songSelection.selected)
                            } else {
                                songSelection
                            }
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun SongSelectionContainer(
    onSelectionChanged: (SongSelectionEntry) -> Unit,
    songSelection: SongSelectionEntry
) {
    Row {
        Checkbox(
            checked = songSelection.selected,
            onCheckedChange = { onSelectionChanged(songSelection)  }
        )
        // TODO change from using SongContainer to some other custom composable for this screen
        SongContainer(
            song = songSelection.song,
            onSongClick = { onSelectionChanged(songSelection) },
        )
    }
}