package com.example.sound.ui.loginPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.R
import com.example.sound.ui.loginPage.authService.AuthUiState
import com.example.sound.ui.loginPage.authService.AuthViewModel
import com.example.sound.ui.loginPage.authService.AuthViewModelFactory
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement


@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory()),
    onLoginButtonClicked: (String, String) -> Unit,
    onLoginSuccess: (String) -> Unit,
    onSignupRouteClicked: () -> Unit = {},
) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val authUiState = authViewModel.authUiState
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val primaryPurple = Color(0xFF7d32a8)
    val onPrimaryWhite = Color.White
    val errorRed = MaterialTheme.colorScheme.error
    val successGreen = Color(0xFF4CAF50)

    val customOutlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryPurple,
        unfocusedBorderColor = primaryPurple.copy(alpha = 0.5f),
        cursorColor = primaryPurple,
        focusedTextColor = primaryPurple,
        unfocusedTextColor = primaryPurple.copy(alpha = 0.7f),
        focusedLabelColor = primaryPurple,
        unfocusedLabelColor = primaryPurple.copy(alpha = 0.5f)
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.logo),
            contentDescription = "SoUNd Logo",
            modifier = Modifier.height(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Welcome to SoUNd",
            fontSize = 28.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = primaryPurple,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = user,
            onValueChange = { text -> user = text },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = primaryPurple,
                fontSize = 16.sp
            ),
            colors = customOutlinedTextFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { text -> pass = text },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = primaryPurple,
                fontSize = 16.sp
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            colors = customOutlinedTextFieldColors
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Register",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .clickable(
                    onClick = onSignupRouteClicked
                )
                .align(Alignment.End)
                .padding(end = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onLoginButtonClicked(user, pass)
                authViewModel.login(user, pass)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryPurple),
            shape = RoundedCornerShape(50),
            enabled = authUiState !is AuthUiState.Loading
        ) {
            if (authUiState is AuthUiState.Loading) {
                CircularProgressIndicator(color = onPrimaryWhite, modifier = Modifier.size(24.dp))
            } else {
                Text("Login", color = onPrimaryWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (authUiState) {
            is AuthUiState.Loading -> Text("Đang đăng nhập...", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            is AuthUiState.Error -> Text("Lỗi: ${authUiState.message}", color = errorRed, style = MaterialTheme.typography.bodySmall)
            is AuthUiState.Success -> {
                Text("Đăng nhập thành công!", color = successGreen, style = MaterialTheme.typography.bodySmall)
                LaunchedEffect(Unit) {
                    onLoginSuccess(authUiState.token)
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Quên mật khẩu? Nhấn vào đây",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp,
            color = primaryPurple,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(Modifier.padding(top = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Đăng nhập bằng Google",
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .clickable {  }
            )
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "Đăng nhập bằng Facebook",
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .clickable {  }
            )
        }
    }
}