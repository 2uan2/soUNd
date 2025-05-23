package com.example.sound.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.sound.data.database.dao.SongDao
import com.example.sound.data.database.model.Song
import com.example.sound.data.database.model.toLocalSong
import com.example.sound.data.network.RetrofitInstance
import com.example.sound.data.network.model.SongUploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

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

    suspend fun uploadSong(
        songName: String,
        artistName: String,
        song: File,
        duration: Long,
        image: File,
    ): Result<SongUploadResponse>

    suspend fun getRemoteSongs(): Result<List<Song>>



}

class LocalSongDataSource(
    private val songDao: SongDao,
    private val mediaStore: MediaStoreDataSource,
    private val tokenManager: TokenRepository,
) : BaseSongDataSource {
    override fun getSong(id: Long): Flow<Song> = songDao.getSong(id)

    override fun getSong(uri: String): Flow<Song> = songDao.getSong(uri)

    override fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()

    override fun getAllSongsByArtist(artist: String): Flow<List<Song>> = songDao.getAllSongsByArtist(artist)

    override fun getAllArtists(): Flow<List<String>> = songDao.getAllArtists()

    override suspend fun insertSong(song: Song) = songDao.insert(song)

    override suspend fun deleteSong(song: Song) = songDao.delete(song)

    override suspend fun updateSong(song: Song) = songDao.update(song)

    override suspend fun getRemoteSongs(): Result<List<Song>> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("missing token"))

        return try {
            val response = RetrofitInstance.songApi.getRemoteSongs("Token $token")
            if (response.isSuccessful) {
                val dtoList = response.body() ?: emptyList()
                val songs = dtoList.map { it.toLocalSong() }
                Result.success(songs)
            } else {
                Result.failure(Exception("Request failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun refreshSongs() {
        val songs = mediaStore.loadSongFromMediaStore()
        songDao.insertAll(songs)
    }

    override suspend fun uploadSong(
        songName: String,
        artistName: String,
        song: File,
        duration: Long,
        image: File,
    ): Result<SongUploadResponse> {
        val token = tokenManager.getToken() ?: return Result.failure(Exception("missing token"))
        return try {
            Log.i("BaseSongDataSource", "token is $token")
            val response = RetrofitInstance.songApi.uploadSong(
                authString = "Token $token",
                songName = songName.toRequestBody("text/plain".toMediaTypeOrNull()),
                artistName = artistName.toRequestBody("text/plain".toMediaTypeOrNull()),
                duration = duration.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                song = MultipartBody.Part
                    .createFormData(
                        "file",
                        song.name,
                        song.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                    ),
                image = MultipartBody.Part
                    .createFormData(
                        "cover_image",
                        image.name,
                        image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    ),
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else Result.failure(Exception("Request failed: ${response.code()}, ${response.message()}"))
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(Exception("IOException: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.failure(Exception("HttpException: ${e.localizedMessage}"))
        }
    }
}