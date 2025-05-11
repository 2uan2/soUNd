package com.example.sound.ui.player

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.example.sound.data.database.model.PlaylistWithSongs
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BasePlaylistDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.example.sound.data.database.model.Playlist

class PlayerViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    private val mediaController: MediaController?
) : ViewModel() {

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _currentPlaylist = MutableStateFlow<PlaylistWithSongs?>(null)
    val currentPlaylist: StateFlow<PlaylistWithSongs?> = _currentPlaylist.asStateFlow()


    var isShuffling by mutableStateOf(false)
        private set

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    val mediaControllerInstance: MediaController?
        get() = mediaController

    enum class RepeatMode {
        NONE,
        REPEAT_ALL,
        REPEAT_ONE
    }

    var repeatMode by mutableStateOf(RepeatMode.NONE)
        private set

    fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            RepeatMode.NONE -> RepeatMode.REPEAT_ALL
            RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
            RepeatMode.REPEAT_ONE -> RepeatMode.NONE
        }
    }

    fun toggleShuffle() {
        isShuffling = !isShuffling
    }


    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistDataSource.getPlaylistWithSongs(playlistId).collect { playlistWithSongs ->
                _currentPlaylist.value = playlistWithSongs
                playlistWithSongs?.songs?.firstOrNull()?.let { playSong(it) }
            }
        }
    }

    fun playSong(song: Song, onPrepared: (MediaController) -> Unit = {}) {
        viewModelScope.launch {
            val currentSongs = _currentPlaylist.value?.songs
            val isSongInCurrentPlaylist = currentSongs?.any { it.songId == song.songId } == true

            if (!isSongInCurrentPlaylist) {
                // Treat this as a custom single-song playlist
                _currentPlaylist.value = PlaylistWithSongs(
                    playlist = Playlist(playlistId = -1, name = "Single Song"),
                    songs = listOf(song)
                )
            }

            _currentSong.value = song
            mediaController?.let { controller ->
                val mediaItem = MediaItem.Builder()
                    .setUri(song.songUri.toUri())
                    .setMediaId(song.songUri)
                    .build()
                controller.setMediaItem(mediaItem)
                controller.prepare()
                controller.play()
                _isPlaying.value = true
                onPrepared(controller)
            }
        }
    }

    fun playNext() {
        val songs = _currentPlaylist.value?.songs ?: return
        val currentIndex = songs.indexOfFirst { it.songUri == _currentSong.value?.songUri }

        if (isShuffling) {
            val randomSong = songs.random()
            playSong(randomSong)
        } else {
            if (currentIndex >= 0 && currentIndex + 1 < songs.size) {
                playSong(songs[currentIndex + 1])
            } else if (repeatMode == RepeatMode.REPEAT_ALL && songs.isNotEmpty()) {
                playSong(songs.first())
            }
        }
    }

    fun playPrevious() {
        val songs = _currentPlaylist.value?.songs ?: return
        val currentIndex = songs.indexOfFirst { it.songUri == _currentSong.value?.songUri }

        if (isShuffling) {
            val randomSong = songs.random()
            playSong(randomSong)
        } else {
            if (currentIndex > 0) {
                playSong(songs[currentIndex - 1])
            } else if (repeatMode == RepeatMode.REPEAT_ALL && songs.isNotEmpty()) {
                playSong(songs.last())
            }
        }
    }

    fun setCustomPlaylist(songs: List<Song>, startSong: Song) {
        _currentPlaylist.value = PlaylistWithSongs(
            playlist = Playlist(playlistId = -1, name = "Custom Playlist"),
            songs = songs
        )
        playSong(startSong)
    }

    fun isCurrentPlaylist(playlistId: Long): Boolean {
        return _currentPlaylist.value?.playlist?.playlistId == playlistId
    }

    fun resetIfCurrentPlaylistIs(playlistId: Long, fallbackSongs: List<Song>) {
        if (isCurrentPlaylist(playlistId)) {
            val fallbackSong = fallbackSongs.firstOrNull() ?: return
            setCustomPlaylist(fallbackSongs, fallbackSong)
        }
    }

    fun pause() {
        mediaController?.pause() ?: Log.w("PlayerViewModel", "MediaController not connected")
        _isPlaying.value = false
    }

    fun resume() {
        mediaController?.let { controller ->
            // Nếu chưa có media item hoặc đang ở trạng thái idle, cần gọi prepare
            if (controller.currentMediaItem == null) {
                _currentSong.value?.let { song ->
                    val mediaItem = MediaItem.Builder()
                        .setUri(song.songUri.toUri())
                        .setMediaId(song.songUri)
                        .build()
                    controller.setMediaItem(mediaItem)
                    controller.prepare()
                }
            }
            controller.play()
            _isPlaying.value = true
        } ?: Log.w("PlayerViewModel", "MediaController not connected")
    }


}
