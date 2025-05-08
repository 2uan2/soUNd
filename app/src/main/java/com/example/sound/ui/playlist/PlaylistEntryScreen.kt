package com.example.sound.ui.playlist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import com.example.sound.ui.home.SongContainer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


@Composable
fun PlaylistEntryScreen(
    onCreateButtonClicked: () -> Unit,
    viewModel: PlaylistEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var playlistName by remember { mutableStateOf(uiState.playlistName) }
    var songSelections by remember { mutableStateOf(uiState.songSelections) }

    LaunchedEffect(uiState.songSelections) {
        songSelections = uiState.songSelections
    }

    Column(
        modifier = Modifier
            .padding(16.dp) // Padding for spacing
            .fillMaxSize(), // Fill available space
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title and Subtitle
        Text(
            text = "Create Playlist",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Playlist Name Input
        TextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            label = { Text("Playlist Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Songs List with checkboxes
        LazyColumn(
            modifier = Modifier.weight(1f) // Take up remaining space
        ) {
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

        // Create Playlist Button
        Button(
            onClick = {
                viewModel.setPlaylistName(playlistName)
                viewModel.setSongSelection(songSelections)
                viewModel.createPlaylist()
                onCreateButtonClicked()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Playlist")
        }
    }
}

@Composable
fun SongSelectionContainer(
    onSelectionChanged: (SongSelectionEntry) -> Unit,
    songSelection: SongSelectionEntry
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Add padding between each item
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = songSelection.selected,
            onCheckedChange = { onSelectionChanged(songSelection) },
            modifier = Modifier.padding(end = 16.dp)
        )

        // You may use your custom SongContainer composable or simply display the song title here
        SongContainer(
            song = songSelection.song,
            onSongClick = { onSelectionChanged(songSelection) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
