package com.example.sound.ui.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs
import com.example.sound.data.repository.BasePlaylistDataSource
import com.example.sound.ui.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlaylistDetailViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val playlistId: Long = checkNotNull(savedStateHandle[Screen.ARG_PLAYLIST_ID])
    private val _uiState: StateFlow<PlaylistWithSongs> =
        playlistDataSource.getPlaylistWithSongs(playlistId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = PlaylistWithSongs(Playlist(name = "unknown"), listOf())
            )
    val uiState = _uiState
}