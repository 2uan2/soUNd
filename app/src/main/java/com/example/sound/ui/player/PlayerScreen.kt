package com.example.sound.ui.player

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sound.R
import com.example.sound.musicService.rememberMediaController
import com.example.sound.ui.AppViewModelProvider
import com.example.sound.ui.player.PlayerViewModel.RepeatMode
import kotlinx.coroutines.delay
import androidx.core.net.toUri


@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val controller = rememberMediaController(context)
    val currentSong by playerViewModel.currentSong.collectAsState()
    val albumId = currentSong?.albumId

    var isPlaying by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    var position by remember { mutableLongStateOf(0L) }
    var userSeeking by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val isShuffling by remember { derivedStateOf { playerViewModel.isShuffling } }
    val repeatMode by remember { derivedStateOf { playerViewModel.repeatMode } }

    // Gán controller cho ViewModel
    LaunchedEffect(controller) {
        controller?.let {
            playerViewModel.setMediaController(it)
        }
    }
    // Theo dõi bài hát hiện tại và khởi động phát
    LaunchedEffect(currentSong, controller) {
        currentSong?.let { song ->
            if (controller?.currentMediaItem?.mediaId != song.songUri) {
                playerViewModel.playSong(song)
                isPrepared = true
                isPlaying = true
            }
        }
    }

    // Theo dõi vị trí phát
    LaunchedEffect(controller, isPlaying) {
        while (true) {
            if (!userSeeking && isPlaying) {
                controller?.let {
                    position = it.currentPosition
                    duration = it.duration.coerceAtLeast(0L)
                    sliderPosition = if (duration > 0) position.toFloat() / duration else 0f

                    // 👇 Check kết thúc bài
                    if (position >= duration && duration > 0) {
                        when (playerViewModel.repeatMode) {
                            RepeatMode.REPEAT_ONE -> {
                                controller.seekTo(0)
                                controller.play()
                            }

                            else -> playerViewModel.playNext()
                        }
                    }
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
            val albumArtUri = currentSong?.albumId?.let {
                ContentUris.withAppendedId("content://media/external/audio/albumart".toUri(), it)
            }

            val coverImageUri = currentSong?.coverImage?.toUri()

            val imageModel = when {
                albumArtUri != null -> albumArtUri
                coverImageUri != null -> coverImageUri
                else -> R.drawable.album_art
            }

            val imagePainter = rememberAsyncImagePainter(
                model = imageModel,
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
                IconButton(onClick = { playerViewModel.toggleShuffle() }) {
                    Icon(
                        Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffling) Color.Green else Color.Gray
                    )
                }
                IconButton(onClick = { playerViewModel.playPrevious() }) {
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
                IconButton(onClick = { playerViewModel.playNext() }) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { playerViewModel.toggleRepeat() }) {
                    Icon(
                        Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = when (repeatMode) {
                            RepeatMode.REPEAT_ALL -> Color.Green
                            RepeatMode.REPEAT_ONE -> Color.Yellow
                            else -> Color.Gray
                        }
                    )
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
