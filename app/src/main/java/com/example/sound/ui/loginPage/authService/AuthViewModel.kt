package com.example.sound.ui.loginPage.authService

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var authState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authState = AuthUiState.Loading
            val result = repository.login(username, password)
            authState = result.fold(
                onSuccess = { AuthUiState.Success(it.token) },
                onFailure = { AuthUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authState = AuthUiState.Loading
            val result = repository.register(username, email, password)
            authState = result.fold(
                onSuccess = { AuthUiState.Success(it.token) },
                onFailure = { AuthUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun reset() {
        authState = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
