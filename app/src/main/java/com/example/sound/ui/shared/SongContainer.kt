package com.example.sound.ui.shared

import android.annotation.SuppressLint
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.sound.R
import com.example.sound.data.database.model.Song
import com.example.sound.ui.home.SongUploadUiState
import com.example.sound.ui.loginPage.authService.AuthState
import java.io.File


@Composable
fun SongContainer(
    song: Song,
    authState: AuthState = AuthState.Unauthenticated,
    songUploadUiState: SongUploadUiState = SongUploadUiState.Idle,
    onSongClick: (Song) -> Unit,
    onSongShareClick: (Song, File, File) -> Unit = { _, _, _ -> },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasAlbumArt = remember(song.albumId) {
        mutableStateOf(false)
    }

    LaunchedEffect(song.albumId) {
        song.albumId?.let {
            hasAlbumArt.value = isAlbumArtAvailable(context, it)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClick(song) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Art
            val albumArtUri = if (hasAlbumArt.value && song.albumId != null) {
                ContentUris.withAppendedId(
                    "content://media/external/audio/albumart".toUri(),
                    song.albumId
                )
            } else {
                "android.resource://${context.packageName}/${R.drawable.faker1}".toUri()
            }


            AsyncImage(
                model = albumArtUri,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Song Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = song.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist ?: "Unknown artist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Duration
            Text(
                text = FormatTime(song.duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Upload Button (if authenticated)
            if (authState == AuthState.Authenticated) {
                IconButton(
                    onClick = {
                        val songFile = File(context.cacheDir, "${song.name}.mp3")
                        songFile.createNewFile()
                        context.contentResolver.openInputStream(song.songUri.toUri())
                            ?.use { inputStream ->
                                songFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            ?: throw IllegalArgumentException("Cannot open input stream from: ${song.songUri}")

                        val albumArtFile = File(context.cacheDir, "albumArt.jpg")
                        if (hasAlbumArt.value && song.albumId != null) {
                            // Trường hợp có album art từ MediaStore
                            context.contentResolver.openInputStream(albumArtUri)
                                ?.use { inputStream ->
                                    albumArtFile.outputStream().use { outputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                        } else {
                            // Trường hợp không có album art → dùng ảnh mặc định từ drawable
                            val bitmap =
                                BitmapFactory.decodeResource(context.resources, R.drawable.faker1)
                            albumArtFile.outputStream().use { outputStream ->
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            }
                        }

                        onSongShareClick(song, songFile, albumArtFile)
                    }, modifier = Modifier.size(40.dp)
                ) {
                    when (songUploadUiState) {
                        is SongUploadUiState.Error -> {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Upload Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        SongUploadUiState.Idle -> {
                            Icon(
                                Icons.Default.ArrowUpward,
                                contentDescription = "Upload Song",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        SongUploadUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is SongUploadUiState.Success -> {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = "Upload Success",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Toast.makeText(
                                context, songUploadUiState.song.songUri, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
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
        onSongClick = {},
        onSongShareClick = { _, _, _ ->

        },
    )
}