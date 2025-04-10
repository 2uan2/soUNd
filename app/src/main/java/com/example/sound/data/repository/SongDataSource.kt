package com.example.sound.data.repository

import com.example.sound.data.database.dao.SongDao
import com.example.sound.data.database.model.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource {
    fun getSong(id: Long): Flow<Song>

    fun getAllSong(): Flow<List<Song>>

    suspend fun insertItem(song: Song)

    suspend fun deleteItem(song: Song)

    suspend fun updateItem(song: Song)

    suspend fun refreshSongs()
}

class LocalSongDataSource(
    private val songDao: SongDao,
    private val mediaStore: MediaStoreDataSource
) : SongDataSource {
    override fun getSong(id: Long): Flow<Song> = songDao.getItem(id)

    override fun getAllSong(): Flow<List<Song>> = songDao.getAllItems()

    override suspend fun insertItem(song: Song) = songDao.insert(song)

    override suspend fun deleteItem(song: Song) = songDao.delete(song)

    override suspend fun updateItem(song: Song) = songDao.update(song)

    override suspend fun refreshSongs() {
        val songs = mediaStore.loadSongFromMediaStore()
        songDao.insertAll(songs)
    }
}