package com.example.sound.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.database.model.Song
import com.example.sound.data.repository.BaseSongDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val TAG = "HomeViewModel"
class HomeViewModel(
    private val songDataSource: BaseSongDataSource
) : ViewModel() {
    private val _uiState: StateFlow<HomeUiState> =
        songDataSource.getAllSongs().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    val uiState: StateFlow<HomeUiState> = _uiState
//    init {
//        refreshSongs()
//    }

    fun refreshSongs() {
        viewModelScope.launch {
            Log.i(TAG, "song refreshed")
            Log.i(TAG, "Songs: ${_uiState.value.songs}")
            songDataSource.refreshSongs()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val songs: List<Song> = listOf()
)