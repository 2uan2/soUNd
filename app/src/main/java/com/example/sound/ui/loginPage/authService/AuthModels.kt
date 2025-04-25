package com.example.sound.ui.loginPage.authService

data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val username: String, val password: String)

data class AuthResponse(val token: String)
