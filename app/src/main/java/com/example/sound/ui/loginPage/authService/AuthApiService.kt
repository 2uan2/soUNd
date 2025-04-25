package com.example.sound.ui.loginPage.authService

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/login/")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/register/")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}
