package com.example.sound.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.repository.BasePlaylistDataSource
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AlbumListViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    private val songDataSource: BaseSongDataSource
) : ViewModel() {
    private val _uiState: StateFlow<AlbumListUiState> =
        songDataSource.getAllArtists().map { AlbumListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = AlbumListUiState()
            )

    val uiState: StateFlow<AlbumListUiState> = _uiState
}

data class AlbumListUiState (
    val albums: List<String> = listOf()
)