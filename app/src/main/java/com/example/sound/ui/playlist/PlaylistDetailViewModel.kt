package com.example.sound.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs
import com.example.sound.data.repository.BasePlaylistDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class PlaylistDetailUiState(
    val playlist: Playlist = Playlist(name = ""),
    val songs: List<com.example.sound.data.database.model.Song> = emptyList()
)

class PlaylistDetailViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playlistId: Long = checkNotNull(savedStateHandle["playlistId"])
    private val _uiState = MutableStateFlow(PlaylistDetailUiState())
    val uiState: StateFlow<PlaylistDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            playlistDataSource.getPlaylistWithSongs(playlistId).collectLatest { playlistWithSongs ->
                if (playlistWithSongs != null) {
                    _uiState.value = PlaylistDetailUiState(
                        playlist = playlistWithSongs.playlist,
                        songs = playlistWithSongs.songs
                    )
                }
            }

        }
    }

    /**
     * Xoá playlist hiện tại. Callback `onDeleted` sẽ được gọi sau khi xoá thành công.
     */
    fun deletePlaylist(onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            playlistDataSource.deletePlaylist(_uiState.value.playlist)
            onDeleted()
        }
    }
}
