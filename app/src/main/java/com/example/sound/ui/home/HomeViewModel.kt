package com.example.sound.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

const val TAG = "HomeViewModel"

class HomeViewModel(
    private val songDataSource: BaseSongDataSource,
) : ViewModel() {
    private val _uiState: StateFlow<HomeUiState> =
        songDataSource.getAllSongs().map {
            HomeUiState(it.map { song -> SongContainerUiState(song = song) })
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    val uiState: StateFlow<HomeUiState> = _uiState

    val songs: StateFlow<List<Song>> = _uiState.map { it.songContainers.map { it.song } }
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())
//    init {
//        refreshSongs()
//    }

    fun refreshSongs() {
        viewModelScope.launch {
            Log.i(TAG, "song refreshed")
            Log.i(TAG, "Songs: ${_uiState.value.songContainers}")
            songDataSource.refreshSongs()
        }
    }

    fun uploadSong(song: Song, songFile: File, albumArtFile: File) {
        _uiState.map { it.songContainers }
        _uiState.value

        viewModelScope.launch {
            songDataSource.uploadSong(
                songName = song.name,
                artistName = song.artist ?: "Unknown artist",
                song = songFile,
                duration = song.duration,
                image = albumArtFile,
            )
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val songContainers: List<SongContainerUiState> = listOf()
)

data class SongContainerUiState(
    val id: Long = song.songId,
    val song: Song,
    val songUploadState: SongUploadUiState = SongUploadUiState.Idle
)

sealed interface SongUploadUiState {
    object Idle : SongUploadUiState
    object Loading : SongUploadUiState
    data class Success(val song: Song) : SongUploadUiState
    data class Error(val message: String) : SongUploadUiState
}

