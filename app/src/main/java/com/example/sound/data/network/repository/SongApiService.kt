package com.example.sound.data.network.repository

import com.example.sound.data.database.model.RemoteSongDto
import com.example.sound.data.network.model.SongUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface SongApiService {
    @Multipart
    @POST("song/")
    suspend fun uploadSong(
        @Header("Authorization") authString: String,
        @Part("name") songName: RequestBody,
        @Part("artist") artistName: RequestBody,
        @Part song: MultipartBody.Part,
        @Part("duration") duration: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<SongUploadResponse>

    @GET("song/")
    suspend fun getRemoteSongs(
        @Header("Authorization") authHeader: String
    ): Response<List<RemoteSongDto>>

    @POST("song/{id}/toggle_favourite/")
    suspend fun toggleFavourite(
        @Path("id") songId: Int,
        @Header("Authorization") authHeader: String
    ): Response<Map<String, Any>>

    // ✅ Thêm mới - lấy danh sách bài hát đã favourite
    @GET("song/favourites/")
    suspend fun getFavouriteSongs(
        @Header("Authorization") authHeader: String
    ): Response<List<RemoteSongDto>>
}