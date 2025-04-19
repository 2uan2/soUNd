package com.example.sound.data.repository

import com.example.sound.data.database.dao.SongDao
import com.example.sound.data.database.model.Song
import kotlinx.coroutines.flow.Flow

interface BaseSongDataSource {
    fun getSong(id: Long): Flow<Song>

    fun getSong(uri: String): Flow<Song>

    fun getAllSongs(): Flow<List<Song>>

    fun getAllSongsByArtist(artist: String): Flow<List<Song>>

    fun getAllArtists(): Flow<List<String>>

    suspend fun insertSong(song: Song)

    suspend fun deleteSong(song: Song)

    suspend fun updateSong(song: Song)

    suspend fun refreshSongs()
}

class LocalSongDataSource(
    private val songDao: SongDao,
    private val mediaStore: MediaStoreDataSource
) : BaseSongDataSource {
    override fun getSong(id: Long): Flow<Song> = songDao.getSong(id)

    override fun getSong(uri: String): Flow<Song> = songDao.getSong(uri)

    override fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()

    override fun getAllSongsByArtist(artist: String): Flow<List<Song>> = songDao.getAllSongsByArtist(artist)

    override fun getAllArtists(): Flow<List<String>> = songDao.getAllArtists()

    override suspend fun insertSong(song: Song) = songDao.insert(song)

    override suspend fun deleteSong(song: Song) = songDao.delete(song)

    override suspend fun updateSong(song: Song) = songDao.update(song)

    override suspend fun refreshSongs() {
        val songs = mediaStore.loadSongFromMediaStore()
        songDao.insertAll(songs)
    }
}