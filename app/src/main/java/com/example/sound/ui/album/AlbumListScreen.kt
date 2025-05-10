package com.example.sound.ui.album

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlbumListScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: AlbumListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val albums = uiState.albums

    Column(

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
//    Column(
//        modifier = Modifier
//            .clickable(onClick = { onAlbumClick(album) })
//    ) {
//        Text(text = album)
//    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .background(Color(0xFF2C2C2C))
            .clickable { onAlbumClick(album) }
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = album,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun AlbumPreview() {
    Album({}, "test")
}