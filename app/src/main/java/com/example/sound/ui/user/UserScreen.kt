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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hello $username",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onDarkModeClicked() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDarkMode) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDarkMode) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black.copy(alpha = 0.8f)
                        )
                    }

                    TopBar()
                    CategoryTabs()
                    LibraryList(items = emptyList())
                }


                Button(
                    onClick = onLogoutButtonClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Log out",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
        AuthState.Unauthenticated -> {
            onUserUnauthenticated()
        }
    }
}