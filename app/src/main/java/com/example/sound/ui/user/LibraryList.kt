package com.example.sound.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sound.data.database.model.Song
import com.example.sound.ui.home.HomeViewModel
import com.example.sound.ui.home.SongContainerUiState
import com.example.sound.ui.loginPage.authService.AuthState
import com.example.sound.ui.shared.RemoteSongContainer

data class LibraryItem(val title: String, val subtitle: String, val imageRes: Int)

@Composable
fun LibraryList(
    authState: AuthState,
    songContainerList: List<SongContainerUiState>,
    onSongClick: (Song) -> Unit,
    viewModel: HomeViewModel
) {
    if (songContainerList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No favorite songs", color = Color.Gray)
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            val filteredSongContainers = songContainerList.filter { (it.song.serverId != -1) && it.song.isFavourited }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.song.name })

            items(filteredSongContainers) { item ->
                RemoteSongContainer(
                    authState = authState,
                    song = item.song,
                    isFavourite = item.song.isFavourited,
                    onFavouriteToggle = { viewModel.onFavouriteToggle(it.serverId) },
                    onSongClick = onSongClick
                )
            }
        }
    }

}
