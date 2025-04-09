package com.example.sound.ui.player

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.media3.common.MediaItem
import com.example.sound.R
import com.example.sound.musicService.rememberMediaController
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen() {
    val context = LocalContext.current
    val songUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"

    val controller = rememberMediaController(context)

    var isPlaying by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    var position by remember { mutableLongStateOf(0L) }
    var userSeeking by remember { mutableStateOf(false) }

    var sliderPosition by remember { mutableFloatStateOf(0f) }


    // Set media item once
    LaunchedEffect(controller) {
        controller?.setMediaItem(MediaItem.fromUri(songUrl))
        controller?.prepare()
        isPrepared = true
    }

    // Update slider every 500ms
    LaunchedEffect(controller, isPlaying) {
        while (true) {
            if (!userSeeking && isPlaying) {
                controller?.let {
                    val currentPos = it.currentPosition
                    val dur = it.duration.coerceAtLeast(0L)

                    position = currentPos
                    duration = dur
                    sliderPosition = if (dur > 0) currentPos.toFloat() / dur else 0f
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
            Image(
                painter = painterResource(id = R.drawable.album_art),
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Song Title", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Artist Name", color = Color.Gray, fontSize = 18.sp)
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
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White, modifier = Modifier.size(40.dp))
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
                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White, modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { /* Repeat */ }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.Gray)
                }
            }
        }
    }
}

//@SuppressLint("DefaultLocale")
//private fun formatTime(seconds: Int): String {
//    val minutes = seconds / 60
//    val remaining = seconds % 60
//    return String.format("%d:%02d", minutes, remaining)
//}

@SuppressLint("DefaultLocale")
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
