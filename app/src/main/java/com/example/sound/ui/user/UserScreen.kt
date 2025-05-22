package com.example.sound.ui.user

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sound.ui.loginPage.authService.AuthState

@Composable
fun LibraryScreen(
    username: String,
    isDarkMode: Boolean,
    onDarkModeClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit,
    onUserUnauthenticated: () -> Unit,
    authState: AuthState = AuthState.Unauthenticated,
) {
    Log.i("LibraryScreen", "authState is $authState")
    when (authState) {
        AuthState.Authenticated -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Hello $username",
                        fontSize = 24.sp
                    )
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { checked ->
                            onDarkModeClicked()
                        }
                    )
                    Button(
                        onClick = onLogoutButtonClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(24),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Log out",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                    TopBar()
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryTabs()
                    Spacer(modifier = Modifier.height(12.dp))
                    LibraryList(items = emptyList())
                }

            }
        }
        AuthState.Unauthenticated -> {
            onUserUnauthenticated()
        }
    }
}
