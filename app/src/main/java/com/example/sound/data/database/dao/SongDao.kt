package com.example.sound.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sound.data.database.model.PlaylistWithSongs
import com.example.sound.data.database.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(song: Collection<Song>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(song: Song)

    @Delete
    suspend fun delete(song: Song)

    @Query("SELECT * FROM songs WHERE songId = :id")
    fun getSong(id: Long): Flow<Song>

    @Query("SELECT * FROM songs WHERE songUri = :uri")
    fun getSong(uri: String): Flow<Song>

    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<Song>>

//    @Query("SELECT * FROM playlists WHERE playlistId = :id")
//    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>
}