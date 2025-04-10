package com.example.sound.data.repository

interface SongRepository {

}

class LocalSongRepository(
    mediaStore: MediaStoreDataSource,
    songStore: SongDataSource,
) : SongRepository {

}