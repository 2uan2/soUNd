package com.example.sound.ui.album

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

@Composable
fun AlbumDetailScreen(
    onSongClick: (Song) -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val albumName = viewModel.albumName

    Column {
        AlbumHeading(albumName)
        SongList(
            songs = uiState.songs,
            onSongClick = onSongClick
        )
    }
}

@Composable
fun AlbumHeading(
    albumName: String,
) {
    Text(text = albumName)
}

@Composable
fun SongList(
    onSongClick: (Song) -> Unit,
    songs: List<Song>
) {
    LazyColumn {
        items(songs) { song ->
            Text(text = song.name)
            SongContainer(
                song = song,
                onSongClick = { onSongClick(song) },
            )
        }
    }
}