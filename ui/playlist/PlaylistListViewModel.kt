package com.example.sound.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs
import com.example.sound.data.repository.BasePlaylistDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlaylistListViewModel(
    private val playlistDataSource: BasePlaylistDataSource
) : ViewModel() {
    private val _uiState: StateFlow<PlaylistListUiState> =
        playlistDataSource.getAllPlaylistWithSongs().map { PlaylistListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = PlaylistListUiState()
            )
    val uiState = _uiState
}

data class PlaylistListUiState(
    val playlists: List<PlaylistWithSongs> = listOf()
)