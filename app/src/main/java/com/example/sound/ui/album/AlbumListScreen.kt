package com.example.sound.ui.album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AlbumListScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: AlbumListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val albums = uiState.albums

    Column(

    ) {
        LazyColumn {
            items(albums) { album ->
                Album(
                    onAlbumClick = { onAlbumClick(album) },
                    album = album
                )
            }
        }
    }
}

@Composable
fun Album(
    onAlbumClick: (String) -> Unit,
    album: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onAlbumClick(album) })
    ) {
        Text(text = album)
    }
}

@Preview
@Composable
fun AlbumPreview() {
    Album({}, "test")
}