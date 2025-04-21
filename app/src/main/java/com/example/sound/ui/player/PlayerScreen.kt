package com.example.sound.ui.player

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import coil.compose.rememberAsyncImagePainter
import com.example.sound.R
import com.example.sound.musicService.rememberMediaController
import kotlinx.coroutines.delay

const val TAG = "PlayerScreen"

@Composable
fun PlayerScreen(playerViewModel: PlayerViewModel = viewModel()) {
    val context = LocalContext.current
    val controller = rememberMediaController(context)

    val currentSong by playerViewModel.currentSong.collectAsState()

    var isPlaying by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    var position by remember { mutableLongStateOf(0L) }
    var userSeeking by remember { mutableStateOf(false) }

    var sliderPosition by remember { mutableFloatStateOf(0f) }


    // Set media item once
    LaunchedEffect(currentSong, controller) {
        currentSong?.let { song ->
            val currentUri = controller?.currentMediaItem?.mediaId
            val targetUri = song.songUri
            if (currentUri != targetUri) {
                controller?.setMediaItem(
                    MediaItem.Builder()
                        .setUri(targetUri.toUri())
                        .setMediaId(targetUri) // ensure mediaId is set
                        .build()
                )
                controller?.prepare()
                controller?.play()
                isPrepared = true
                isPlaying = true
            }
        }
    }

    LaunchedEffect(controller, isPlaying) {
        while (true) {
            if (!userSeeking && isPlaying) {
                controller?.let {
                    position = it.currentPosition
                    duration = it.duration.coerceAtLeast(0L)
                    sliderPosition = if (duration > 0) position.toFloat() / duration else 0f
                }
            }
            delay(500)
        }
    }


    fun togglePlayPause() {
        if (!isPrepared) return
        if (isPlaying) controller?.pause() else controller?.play()
        isPlaying = !isPlaying
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val imagePainter = rememberAsyncImagePainter(
                model = currentSong?.imageUri ?: R.drawable.album_art,
                placeholder = painterResource(id = R.drawable.album_art),
                error = painterResource(id = R.drawable.album_art)
            )

            Image(
                painter = imagePainter,
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    currentSong?.name ?: "No Title",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(currentSong?.artist ?: "Artist Name", color = Color.Gray, fontSize = 18.sp)
            }

            // Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        userSeeking = true
                        sliderPosition = it
                    },
                    onValueChangeFinished = {
                        val seekPos = (sliderPosition * duration).toLong()
                        controller?.seekTo(seekPos)
                        position = seekPos // Sync position
                        userSeeking = false
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        inactiveTrackColor = Color.DarkGray
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatTime(position), color = Color.Gray)
                    Text(formatTime(duration), color = Color.Gray)
                }
            }

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Shuffle */ }) {
                    Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.Gray)
                }
                IconButton(onClick = { /* Previous */ }) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(
                    onClick = { togglePlayPause() },
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.Green, CircleShape)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { /* Next */ }) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { /* Repeat */ }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.Gray)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
