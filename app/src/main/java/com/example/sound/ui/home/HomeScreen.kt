package com.example.sound.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.R
import com.example.sound.data.database.model.Song
import com.example.sound.ui.AppViewModelProvider

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {}
    ) { innerPadding ->
        HomeBody(
            viewModel = viewModel,
            songList = uiState.value.songs,
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun HomeBody(
    viewModel: HomeViewModel,
    songList: List<Song>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column {
        Button(
            onClick = viewModel::refreshSongs
        ) {
            Text(text = "Refresh songs")
        }
//        Spacer(modifier = Modifier.padding(64.dp))
        HorizontalDivider()
        LazyColumn(
            modifier = modifier.padding(contentPadding)
        ) {
            items(songList) { song ->
                SongContainer(song)
            }
        }
    }
}

@Composable
fun SongContainer(
    song: Song,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {  }
    ) {
        Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = null)
        Column (
            modifier = modifier
        ) {
            Text(text = song.name, fontWeight = FontWeight.Bold)
            Text(text = "${song.artist}")
        }
        Spacer(modifier = Modifier.weight(1f))
        // TODO: duration is in milliseconds, need function to turn it into minute:second format
        Text(text = "${song.duration/60000}")
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    SongContainer(Song(
        songUri = "testing",
        name = "song",
        duration = 100000,
    ))
}
























