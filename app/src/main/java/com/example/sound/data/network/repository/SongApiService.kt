package com.example.sound.data.network.repository

import com.example.sound.data.network.model.SongUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
}