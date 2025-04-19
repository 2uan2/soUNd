package com.example.sound.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BaseSongDataSource
import com.example.sound.data.repository.LocalSongDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AlbumDetailViewModel(
    private val songDataSource: BaseSongDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val albumName: String = checkNotNull(savedStateHandle["albumName"])

    private val _uiState: StateFlow<AlbumUiState> =
        songDataSource.getAllSongsByArtist(albumName).map { AlbumUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = AlbumUiState()
            )
    val uiState = _uiState


}

data class AlbumUiState (
    val songs: List<Song> = listOf()
)