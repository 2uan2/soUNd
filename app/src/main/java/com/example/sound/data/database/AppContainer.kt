package com.example.sound.data.database

import android.content.Context
import com.example.sound.data.repository.BasePlaylistDataSource
import com.example.sound.data.repository.LocalSongDataSource
import com.example.sound.data.repository.MediaStoreDataSource
import com.example.sound.data.repository.BaseSongDataSource
import com.example.sound.data.repository.LocalPlaylistDataSource
import com.example.sound.data.repository.TokenRepository

interface AppContainer {
    val songStore: BaseSongDataSource
    val playlistDataSource: BasePlaylistDataSource
    val tokenManager: TokenRepository
//    val mediaStore: MediaStoreDataSource
}

class AppDataContainer(private val context: Context): AppContainer {
    override val songStore: BaseSongDataSource by lazy {
        LocalSongDataSource(
            songDao = AppDatabase.getDatabase(context).songDao(),
            mediaStore = MediaStoreDataSource(context),
            tokenManager = TokenRepository(context),
        )
    }

    override val playlistDataSource: BasePlaylistDataSource by lazy {
        LocalPlaylistDataSource(
            playlistDao = AppDatabase.getDatabase(context).playlistDao()
        )
    }

    override val tokenManager: TokenRepository by lazy {
        TokenRepository(context = context)
    }

//    override val mediaStore: MediaStoreDataSource by lazy {
//        MediaStoreDataSource(context)
//    }
}
