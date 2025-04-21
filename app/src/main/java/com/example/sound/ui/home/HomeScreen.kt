package com.example.sound.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.R
import com.example.sound.data.database.model.Song
import com.example.sound.ui.AppViewModelProvider
import com.example.sound.ui.player.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    var queryText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }

    val songList = uiState.songs.filter {
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
            onSongClick = onSongClick,
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
            items(songList) { song ->
                SongContainer(
                    song = song,
                    onSongClick = onSongClick
                )
            }
        }
    }
}

@Composable
fun SongContainer(
    song: Song,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClick(song) }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null
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
            Text(text = "${song.artist}")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = formatTime(song.duration))
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
        onSongClick = {}
    )
}
