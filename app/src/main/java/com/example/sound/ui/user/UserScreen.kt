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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
// Các import cần thiết đã được bổ sung/kiểm tra
import androidx.compose.material3.ExperimentalMaterial3Api // Đảm bảo dòng này có mặt
import androidx.compose.material3.CenterAlignedTopAppBar // Đảm bảo dòng này có mặt
import androidx.compose.material3.TopAppBarDefaults // Đảm bảo dòng này có mặt
import androidx.compose.material3.SwitchDefaults // Đảm bảo dòng này có mặt
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
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
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Hello, $username",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(

                            containerColor = MaterialTheme.colorScheme.surface, // Thay thế ở đây
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Button(
                            onClick = onLogoutButtonClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = "Log out",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

//                    Text(
//                        text = "Hello, $username",
//                        style = MaterialTheme.typography.displaySmall,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.onBackground,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                // Sửa lỗi: Nếu surfaceContainerHigh không được giải quyết, hãy thay thế
                                // bằng MaterialTheme.colorScheme.surface hoặc MaterialTheme.colorScheme.surfaceVariant
                                color = MaterialTheme.colorScheme.surfaceContainerHigh, // Kiểm tra dòng này
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = if (isDarkMode) "Dark Mode" else "Light Mode",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onDarkModeClicked() },
                            // SwitchDefaults đã được import ở trên
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Music Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Placeholder cho CategoryTabs
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                // Sửa lỗi: Nếu surfaceContainer không được giải quyết, hãy thay thế
                                // bằng MaterialTheme.colorScheme.surface hoặc MaterialTheme.colorScheme.surfaceVariant
                                MaterialTheme.colorScheme.surfaceContainer, // Kiểm tra dòng này
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Category Tabs Placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Library Content",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Placeholder cho LibraryList
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                // Sửa lỗi: Nếu surfaceContainer không được giải quyết, hãy thay thế
                                // bằng MaterialTheme.colorScheme.surface hoặc MaterialTheme.colorScheme.surfaceVariant
                                MaterialTheme.colorScheme.surfaceContainer, // Kiểm tra dòng này
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Library List Placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                    }
                }
            }
        }
        AuthState.Unauthenticated -> {
            onUserUnauthenticated()
        }
    }
}