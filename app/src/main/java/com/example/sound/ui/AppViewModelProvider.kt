package com.example.sound.ui

/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sound.SoundApplication
import com.example.sound.ui.album.AlbumDetailViewModel
import com.example.sound.ui.album.AlbumListViewModel
import com.example.sound.ui.home.HomeViewModel
import com.example.sound.ui.player.PlayerViewModel
import com.example.sound.ui.playlist.PlaylistDetailViewModel
import com.example.sound.ui.playlist.PlaylistEntryViewModel
import com.example.sound.ui.playlist.PlaylistListViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(SoundApplication.container.songStore)
        }

        initializer {
            AlbumListViewModel(
                SoundApplication.container.playlistDataSource,
                SoundApplication.container.songStore
            )
        }

        initializer {
            AlbumDetailViewModel(
                SoundApplication.container.songStore,
                this.createSavedStateHandle()
            )
        }

        initializer {
            PlaylistListViewModel(
                SoundApplication.container.playlistDataSource,

                )
        }

        initializer {
            PlaylistEntryViewModel(
                SoundApplication.container.playlistDataSource,
                SoundApplication.container.songStore,
            )
        }

        initializer {
            PlaylistDetailViewModel(
                SoundApplication.container.playlistDataSource,
                this.createSavedStateHandle(),
            )
        }

        initializer {
            PlayerViewModel(
                playlistDataSource = SoundApplication.container.playlistDataSource,
                mediaController = SoundApplication.mediaController
            )
        }
    }
}