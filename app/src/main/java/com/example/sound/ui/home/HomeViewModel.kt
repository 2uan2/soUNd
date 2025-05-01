package com.example.sound.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

const val TAG = "HomeViewModel"

class HomeViewModel(
    private val songDataSource: BaseSongDataSource,
) : ViewModel() {
//    private val _uiState: StateFlow<HomeUiState> =
//        songDataSource.getAllSongs().map {
//            HomeUiState(it.map { song -> SongContainerUiState(song = song) })
//        }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState()
//            )

    private val _songs: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _uploadStates: MutableStateFlow<Map<Long, SongUploadUiState>> = MutableStateFlow(emptyMap())
    val uploadState: StateFlow<Map<Long, SongUploadUiState>> = _uploadStates

    init {
        viewModelScope.launch {
            songDataSource.getAllSongs().collect { songs ->
                _songs.value = songs
//                _uploadStates.value = List(songs.size) { SongUploadUiState.Idle }
            }
        }
    }

     val uiState: StateFlow<HomeUiState> =
        combine(_songs, _uploadStates) { songs, uploadStates ->
            val container = songs.map { song ->
                SongContainerUiState(
                    song = song,
                    songUploadState = uploadStates.getOrDefault(song.songId, SongUploadUiState.Idle)
                )
            }
            HomeUiState(container)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HomeUiState()
        )

//    init {
//        refreshSongs()
//    }

    fun refreshSongs() {
        viewModelScope.launch {
            Log.i(TAG, "song refreshed")
            Log.i(TAG, "Songs: ${uiState.value.songContainers}")
            songDataSource.refreshSongs()
        }
    }

    fun uploadSong(song: Song, songFile: File, albumArtFile: File) {
        viewModelScope.launch {
            _uploadStates.update { currentMap ->
                val newMap = currentMap.toMutableMap()
                newMap[song.songId] = SongUploadUiState.Loading
                newMap
            }
            val result = songDataSource.uploadSong(
                songName = song.name,
                artistName = song.artist ?: "Unknown artist",
                song = songFile,
                duration = song.duration,
                image = albumArtFile,
            )
            _uploadStates.update { currentMap ->
                val newMap = currentMap.toMutableMap()
                result.fold(
                    onSuccess = { newMap[song.songId] = SongUploadUiState.Success(Song(
                        songUri = it.songUrl,
                        name = it.name,
                        duration = it.duration,
                    ))},
                    onFailure = { newMap[song.songId] = SongUploadUiState.Error(it.message ?: "unknown error")}
                )
                newMap
            }
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
    val song: Song,
    val id: Long = song.songId,
    val songUploadState: SongUploadUiState = SongUploadUiState.Idle
)

sealed interface SongUploadUiState {
    object Idle : SongUploadUiState
    object Loading : SongUploadUiState
    data class Success(val song: Song) : SongUploadUiState
    data class Error(val message: String) : SongUploadUiState
}

