package com.example.sound.ui.playlist

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BasePlaylistDataSource
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlaylistEntryViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    private val songDataSource: BaseSongDataSource
) : ViewModel() {
    private val _playlistName = MutableStateFlow("")

    fun setPlaylistName(newName: String) {
        _playlistName.value = newName
    }

    private val _songSelectionsFlow: Flow<List<SongSelectionEntry>> =
        songDataSource.getAllSongs().map { songs ->
            songs.map { song -> SongSelectionEntry(song, false) }
        }

    val uiState: StateFlow<PlaylistEntryUiState> =
        combine(_playlistName, _songSelectionsFlow) { name, songs ->
            PlaylistEntryUiState(name, songs)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PlaylistEntryUiState()
        )
//    val uiState: StateFlow<PlaylistEntryUiState> = _uiState


}

data class PlaylistEntryUiState (
    val playlistName: String = "",
    val songSelections: List<SongSelectionEntry> = listOf()
)

data class SongSelectionEntry (
    val song: Song,
    val selected: Boolean,
)

