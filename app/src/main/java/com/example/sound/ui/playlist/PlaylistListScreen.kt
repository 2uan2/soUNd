package com.example.sound.ui.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert // Added for potential future options
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistListScreen(
    onPlaylistClicked: (Playlist) -> Unit,
    onCreatePlaylistButtonClicked: () -> Unit,
    viewModel: PlaylistListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Changed to CenterAlignedTopAppBar for better aesthetic
                title = {
                    Text(
                        "Your Playlists",
                        style = MaterialTheme.typography.headlineMedium, // Slightly larger title
                        fontWeight = FontWeight.Bold // Stronger emphasis
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp), // Dynamic color based on elevation
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePlaylistButtonClicked,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = FloatingActionButtonDefaults.largeShape, // Use default large shape for consistency
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Playlist")
            }
        }
    ) { paddingValues ->
        if (uiState.playlists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 24.dp) // Add horizontal padding for text
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.PlaylistPlay,
                        contentDescription = null,
                        modifier = Modifier.size(96.dp), // Larger icon for empty state
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) // Slightly lighter tint
                    )
                    Spacer(modifier = Modifier.height(20.dp)) // Increased spacing
                    Text(
                        text = "You don't have any playlists yet.\nTap the '+' button to create one!", // More descriptive text
                        style = MaterialTheme.typography.titleMedium, // Better typography for main message
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center // Center align text
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Added vertical padding
                verticalArrangement = Arrangement.spacedBy(10.dp), // Slightly adjusted spacing
                contentPadding = PaddingValues(bottom = 80.dp) // Add padding to bottom to prevent FAB overlap
            ) {
                items(uiState.playlists) { playlist ->
                    PlaylistContainer(
                        playlistWithSongs = playlist,
                        onPlaylistClicked = onPlaylistClicked
                        // Optionally, could add a lambda for "More options" here
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistContainer(
    onPlaylistClicked: (Playlist) -> Unit,
    playlistWithSongs: PlaylistWithSongs
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaylistClicked(playlistWithSongs.playlist) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh // Use a distinct surface color for cards
        ),
        shape = MaterialTheme.shapes.extraSmall, // Sharper corners for a modern feel
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Softer shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.PlaylistPlay,
                contentDescription = null,
                modifier = Modifier.size(40.dp), // Slightly larger icon in list
                tint = MaterialTheme.colorScheme.primary // Use primary color
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playlistWithSongs.playlist.name,
                    style = MaterialTheme.typography.titleLarge, // Larger title for better readability
                    fontWeight = FontWeight.SemiBold, // Stronger emphasis
                    maxLines = 1, // Prevent text overflow
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp)) // Adjusted spacing
                Text(
                    text = "${playlistWithSongs.songs.size} songs",
                    style = MaterialTheme.typography.bodyMedium, // Better body text size
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) // Slightly muted
                )
            }
            // Optional: Add a "More options" icon for each playlist
            IconButton(onClick = { /* Handle more options click */ }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}