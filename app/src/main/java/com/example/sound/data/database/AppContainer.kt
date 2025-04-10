package com.example.sound.data.database

import android.content.Context
import com.example.sound.data.repository.LocalSongDataSource
import com.example.sound.data.repository.MediaStoreDataSource
import com.example.sound.data.repository.SongDataSource

interface AppContainer {
    val songStore: SongDataSource
//    val mediaStore: MediaStoreDataSource
}

class AppDataContainer(private val context: Context): AppContainer {
    override val songStore: SongDataSource by lazy {
        LocalSongDataSource(
            songDao = AppDatabase.getDatabase(context).songDao(),
            mediaStore = MediaStoreDataSource(context)
        )
    }

//    override val mediaStore: MediaStoreDataSource by lazy {
//        MediaStoreDataSource(context)
//    }
}
