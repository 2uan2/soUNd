package com.example.sound.data.repository

import com.example.sound.data.database.dao.PlaylistDao
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistSongCrossRef
import com.example.sound.data.database.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

interface BasePlaylistDataSource {
    fun getPlaylist(id: Long): Flow<Playlist>

    fun getAllPlaylist(): Flow<List<Playlist>>

    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs?>

    fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>>

    suspend fun insertPlaylist(playlist: Playlist): Long

    suspend fun insertCrossRef(crossRef: PlaylistSongCrossRef)

    suspend fun insertCrossRefs(crossRef: List<PlaylistSongCrossRef>)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)
}

class LocalPlaylistDataSource(
    private val playlistDao: PlaylistDao
) : BasePlaylistDataSource {
    override fun getPlaylist(id: Long): Flow<Playlist> {
        return playlistDao.getPlaylist(id)
    }

    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylist()
    }

    override fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs?> {
        return playlistDao.getPlaylistWithSongs(id)
    }

    override fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>> {
        return playlistDao.getAllPlaylistWithSongs()
    }

    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return playlistDao.insert(playlist)
    }

    override suspend fun insertCrossRef(crossRef: PlaylistSongCrossRef) {
        playlistDao.insertCrossRef(crossRef)
    }

    override suspend fun insertCrossRefs(crossRef: List<PlaylistSongCrossRef>) {
        playlistDao.insertCrossRefs(crossRef)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }
}