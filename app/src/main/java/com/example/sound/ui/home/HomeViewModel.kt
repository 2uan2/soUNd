package com.example.sound.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

enum class SongSourceType { LOCAL, REMOTE }

class HomeViewModel(
    private val songDataSource: BaseSongDataSource,
) : ViewModel() {

    private val _localSongs = MutableStateFlow<List<Song>>(emptyList())
    private val _remoteSongs = MutableStateFlow<List<Song>?>(null)

    private val _currentSource = MutableStateFlow(SongSourceType.LOCAL)
    val currentSource: StateFlow<SongSourceType> = _currentSource

    // Add isLoading, isRefreshing, and error StateFlows
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
            _isLoading.value = true // Set loading to true on init
            _error.value = null // Clear any previous error
            try {
                songDataSource.getAllSongs().collect { songs ->
                    _localSongs.value = songs
                }
            } catch (e: Exception) {
                _error.value = "Failed to load local songs: ${e.message}"
                Log.e(TAG, "Error loading local songs: ${e.message}", e)
            } finally {
                _isLoading.value = false // Set loading to false when done
            }
        }
    }

    fun switchSource(type: SongSourceType) {
        _currentSource.value = type
        // When switching to remote, immediately try to load remote songs if not already loaded
        if (type == SongSourceType.REMOTE && _remoteSongs.value == null) {
            loadRemoteSongs()
        }
        _error.value = null // Clear error when switching source
    }

    fun loadRemoteSongs() {
        viewModelScope.launch {
            _isLoading.value = true // Set loading for remote songs
            _error.value = null // Clear any previous error
            try {
                val result = songDataSource.getRemoteSongs()
                result.fold(
                    onSuccess = { _remoteSongs.value = it },
                    onFailure = {
                        _error.value = "Failed to load remote songs: ${it.message}"
                        Log.e(TAG, "Failed to load remote songs: ${it.message}", it)
                    }
                )
            } catch (e: Exception) {
                _error.value = "Failed to load remote songs: ${e.message}"
                Log.e(TAG, "Error loading remote songs: ${e.message}", e)
            } finally {
                _isLoading.value = false // Set loading to false when done
            }
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
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), // Use TIMEOUT_MILLIS
            initialValue = HomeUiState()
        )

    fun refreshSongs() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            Log.i(TAG, "song refreshed")
            try {
                songDataSource.refreshSongs()
                // After refreshing, re-collect local songs or load remote songs depending on current source
                if (_currentSource.value == SongSourceType.LOCAL) {
                    } else {
                    loadRemoteSongs()
                }
            } catch (e: Exception) {
                _error.value = "Failed to refresh songs: ${e.message}"
                Log.e(TAG, "Error refreshing songs: ${e.message}", e)
            } finally {
                _isRefreshing.value = false // Set refreshing to false
            }
        }
    }

    fun uploadSong(song: Song, songFile: File, albumArtFile: File) {
        viewModelScope.launch {
            _uploadStates.update { currentMap ->
                val newMap = currentMap.toMutableMap()
                newMap[song.songId] = SongUploadUiState.Loading
                newMap
            }
            try {
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
                                    artist = it.artist,
//                                    albumId = it.albumId, // Add albumId if available from remote
//                                    isFavourited = it.isFavourited, // Add isFavourited
//                                    serverId = it.id // Use id for serverId if consistent
                                )
                            )
                        },
                        onFailure = {
                            newMap[song.songId] = SongUploadUiState.Error(it.message ?: "unknown error")
                        }
                    )
                    newMap
                }
            } catch (e: Exception) {
                _uploadStates.update { currentMap ->
                    val newMap = currentMap.toMutableMap()
                    newMap[song.songId] = SongUploadUiState.Error(e.message ?: "upload failed due to exception")
                    newMap
                }
                Log.e(TAG, "Upload failed due to exception: ${e.message}", e)
            }
        }
    }

    fun onFavouriteToggle(serverId: Int) { // Changed to Int as per your model often uses Int for server IDs
        viewModelScope.launch {
            try {
                songDataSource.toggleFavourite(serverId)

                if (currentSource.value == SongSourceType.REMOTE) {
                    loadRemoteSongs() // Reload to reflect favorite status change
                }
            } catch (e: Exception) {
                _error.value = "Failed to toggle favorite: ${e.message}"
                Log.e(TAG, "Error toggling favorite: ${e.message}", e)
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
    val id: Long = song.songId, // song.songId for local, song.serverId for remote? Be careful with ID source.
    val songUploadState: SongUploadUiState = SongUploadUiState.Idle
)

sealed interface SongUploadUiState {
    object Idle : SongUploadUiState
    object Loading : SongUploadUiState
    data class Success(val song: Song) : SongUploadUiState // This song object should ideally reflect the remote song details
    data class Error(val message: String) : SongUploadUiState
}