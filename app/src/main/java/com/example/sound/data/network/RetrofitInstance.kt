package com.example.sound.data.network

import com.example.sound.data.network.repository.SongApiService
import com.example.sound.ui.loginPage.authService.AuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.100.96:8000/"

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val songApi: SongApiService by lazy {
        retrofit.create(SongApiService::class.java)
    }
}