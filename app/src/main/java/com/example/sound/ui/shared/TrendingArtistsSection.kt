package com.example.sound.ui.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.sound.R
import com.example.sound.data.database.model.ArtistDto
import com.example.sound.data.database.model.Song
import com.example.sound.ui.home.HomeViewModel
import com.example.sound.ui.player.PlayerViewModel


@Composable
fun TrendingArtistsSection(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    var showAllArtists by remember { mutableStateOf(false) }
    val artists by homeViewModel.artists.collectAsState()

    val artistsToShow = if (showAllArtists) artists else artists.take(5)

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending Artists",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            if (!showAllArtists && artists.size > 5) {
                Text(
                    text = "More",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { showAllArtists = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(artistsToShow) { artist ->
                ArtistCard(
                    artist = artist,
                    homeViewModel = homeViewModel,
                    playerViewModel = playerViewModel
                )
            }
        }
    }
}

//cart for testing artist
@Composable
fun ArtistCard(
    artist: ArtistDto,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,

    onSongClick: (Song) -> Unit = {}
) {
    var artistSongs by remember { mutableStateOf<List<Song>>(emptyList()) }

    // Load songs only once when composed
    LaunchedEffect(artist.name) {
        artistSongs = homeViewModel.getSongsByArtist(artist.name)
    }

    val artistImageUri = artist.avatar.toUri()
    val imagePainter = rememberAsyncImagePainter(
        model = artistImageUri,
        placeholder = painterResource(id = R.drawable.album_art),
        error = painterResource(id = R.drawable.album_art)
    )

    Card(
        modifier = Modifier
            .width(220.dp)
            .height(300.dp)
            .padding(vertical = 8.dp)
            .clickable {
                // When card is clicked, play artist songs
                artistSongs.firstOrNull()?.let { firstSong ->
                    playerViewModel.setCustomPlaylist(artistSongs, firstSong)
                    onSongClick(firstSong)
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Artist Image with overlay text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "Artist Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC000000)),
                                startY = 100f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = artist.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${artistSongs.size} songs",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                    )
                }
            }

            // Show max 2 songs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (artistSongs.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        color = Color.White
                    )
                } else {
                    artistSongs.take(2).forEach { song ->
                        Text(
                            text = song.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFDDDDDD)
                            )
                        )
                    }
                }
            }
        }
    }
}

