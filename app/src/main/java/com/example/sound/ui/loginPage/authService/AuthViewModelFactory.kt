package com.example.sound.ui.loginPage.authService

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sound.SoundApplication
import com.example.sound.data.network.RetrofitInstance

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AuthRepository(RetrofitInstance.authApi)
        return AuthViewModel(repository, SoundApplication.container.tokenManager) as T
    }
}
