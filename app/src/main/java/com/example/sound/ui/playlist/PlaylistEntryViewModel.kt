package com.example.sound.ui.playlist

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistSongCrossRef
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BasePlaylistDataSource
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val TAG = "PlaylistEntryViewModel"
class PlaylistEntryViewModel(
    private val playlistDataSource: BasePlaylistDataSource,
    private val songDataSource: BaseSongDataSource
) : ViewModel() {
    private val _playlistName = MutableStateFlow("")

    private var _songSelections: MutableStateFlow<List<SongSelectionEntry>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch {
            songDataSource.getAllSongs().collect { songs ->
                _songSelections.value = songs.map { song -> SongSelectionEntry(song, false) }
            }
        }
    }

    val uiState: StateFlow<PlaylistEntryUiState> =
        combine(_playlistName, _songSelections) { name, songs ->
            Log.i(TAG, songs.toString())
            PlaylistEntryUiState(name, songs)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PlaylistEntryUiState()
        )
//    val uiState: StateFlow<PlaylistEntryUiState> = _uiState

    fun setPlaylistName(newName: String) {
        _playlistName.value = newName
    }

    fun setSongSelection(songSelections: List<SongSelectionEntry>) {
        _songSelections.value = songSelections
    }

    fun createPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = Playlist(name = _playlistName.value)
            val playlistId = playlistDataSource.insertPlaylist(playlist)
            val crossRefs: MutableList<PlaylistSongCrossRef> = mutableListOf()

            _songSelections.value
                .filter { it.selected }
                .forEach { songSelection ->
                    val crossRef = PlaylistSongCrossRef(playlistId, songSelection.song.songId)
                    crossRefs.add(crossRef)
                }

            playlistDataSource.insertCrossRefs(crossRefs)
        }
    }



}

data class PlaylistEntryUiState (
    val playlistName: String = "",
    val songSelections: List<SongSelectionEntry> = listOf()
)

data class SongSelectionEntry (
    val song: Song,
    val selected: Boolean,
)

