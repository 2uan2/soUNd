package com.example.sound.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistSongCrossRef
import com.example.sound.data.database.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlist: Playlist): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(playlist: Playlist)

    @Delete()
    suspend fun delete(playlist: Playlist)

    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylist(id: Long): Flow<Playlist>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylist(): Flow<List<Playlist>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCrossRef(crossRef: PlaylistSongCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCrossRefs(crossRef: List<PlaylistSongCrossRef>)
}