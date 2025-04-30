package com.example.sound.ui.loginPage.authService

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sound.data.repository.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenRepository,
) : ViewModel() {
    var authUiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    // application state (logged in or not)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authUiState = AuthUiState.Loading
            val result = repository.login(username, password)
            authUiState = result.fold(
                onSuccess = {
                    _authState.value = AuthState.Authenticated
                    tokenManager.putToken(it.token)
                    AuthUiState.Success(it.token)
                },
                onFailure = { AuthUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authUiState = AuthUiState.Loading
            val result = repository.register(username, email, password)
            authUiState = result.fold(
                onSuccess = {
                    _authState.value = AuthState.Authenticated
                    tokenManager.putToken(it.token)
                    AuthUiState.Success(it.token)
                },
                onFailure = { AuthUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
        tokenManager.clearToken()
    }

    fun reset() {
        authUiState = AuthUiState.Idle
    }
}

sealed interface AuthState {
    object Authenticated : AuthState
    object Unauthenticated : AuthState
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
