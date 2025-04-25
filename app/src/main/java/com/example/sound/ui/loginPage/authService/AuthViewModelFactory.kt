package com.example.sound.ui.loginPage.authService

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AuthRepository(RetrofitInstance.api)
        return AuthViewModel(repository) as T
    }
}
