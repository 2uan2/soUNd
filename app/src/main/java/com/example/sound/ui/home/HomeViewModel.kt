package com.example.sound.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.data.database.model.Song
import com.example.sound.data.database.model.toLocalSong
import com.example.sound.data.network.RetrofitInstance
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

enum class SongSourceType { LOCAL, REMOTE }

class HomeViewModel(
    private val songDataSource: BaseSongDataSource,
) : ViewModel() {

    private val _localSongs = MutableStateFlow<List<Song>>(emptyList())
    private val _remoteSongs = MutableStateFlow<List<Song>?>(null) // initially null

    private val _currentSource = MutableStateFlow(SongSourceType.LOCAL)
    val currentSource: StateFlow<SongSourceType> = _currentSource

    val songs: StateFlow<List<Song>> =
        combine(_localSongs, _remoteSongs, _currentSource) { local, remote, source ->
            when (source) {
                SongSourceType.LOCAL -> local
                SongSourceType.REMOTE -> remote ?: emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _uploadStates: MutableStateFlow<Map<Long, SongUploadUiState>> =
        MutableStateFlow(emptyMap())
    val uploadState: StateFlow<Map<Long, SongUploadUiState>> = _uploadStates

    init {
        viewModelScope.launch {
            songDataSource.getAllSongs().collect { songs ->
                _localSongs.value = songs
            }
        }
    }

    fun switchSource(type: SongSourceType) {
        _currentSource.value = type
    }


    fun loadRemoteSongs() {
        viewModelScope.launch {
            val result = songDataSource.getRemoteSongs()
            result.fold(
                onSuccess = { _remoteSongs.value = it },
                onFailure = { Log.e(TAG, "Failed to load remote songs: ${it.message}") }
            )
        }
    }


    val uiState: StateFlow<HomeUiState> =
        combine(songs, _uploadStates) { songList, uploadStates ->
            val container = songList.map { song ->
                SongContainerUiState(
                    song = song,
                    songUploadState = uploadStates.getOrDefault(song.songId, SongUploadUiState.Idle)
                )
            }
            HomeUiState(container)
        }.stateIn(
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
                    onSuccess = {
                        newMap[song.songId] = SongUploadUiState.Success(
                            Song(
                                songUri = it.songUrl,
                                name = it.name,
                                duration = it.duration,
                            )
                        )
                    },
                    onFailure = {
                        newMap[song.songId] = SongUploadUiState.Error(it.message ?: "unknown error")
                    }
                )
                newMap
            }
        }
    }

    fun onFavouriteToggle(serverId: Int) {
        viewModelScope.launch {
            songDataSource.toggleFavourite(serverId)

            if (currentSource.value == SongSourceType.REMOTE) {
                loadRemoteSongs()
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

