package com.example.sound.ui.account

import androidx.compose.runtime.Composable
import com.example.sound.ui.loginPage.authService.AuthState

@Composable
fun AccountScreen(
    authState: AuthState = AuthState.Unauthenticated,

) {
    when (authState) {
        AuthState.Authenticated -> {

        }
        AuthState.Unauthenticated -> {

        }
    }
}