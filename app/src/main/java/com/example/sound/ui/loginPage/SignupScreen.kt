package com.example.sound.ui.loginPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.R
import com.example.sound.ui.loginPage.authService.AuthUiState
import com.example.sound.ui.loginPage.authService.AuthViewModel
import com.example.sound.ui.loginPage.authService.AuthViewModelFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size

@Preview(showBackground = true)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory()),
    onRegisterButtonClicked: (String, String, String) -> Unit = { username, email, password -> },
    onLoginRouteClicked: () -> Unit = {},
    onSignupSuccess: (String) -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authUiState = authViewModel.authUiState

    val primaryPurple = Color(0xFF7d32a8)
    val onPrimaryWhite = Color.White
    val errorRed = MaterialTheme.colorScheme.error
    val successGreen = Color(0xFF4CAF50)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
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
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Create an Account",
            fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = primaryPurple,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
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
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = primaryPurple,
                unfocusedTextColor = primaryPurple,
                cursorColor = primaryPurple,
                focusedLabelColor = primaryPurple,
                unfocusedLabelColor = primaryPurple.copy(alpha = 0.5f)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = primaryPurple,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = primaryPurple,
                unfocusedTextColor = primaryPurple,
                cursorColor = primaryPurple,
                focusedLabelColor = primaryPurple,
                unfocusedLabelColor = primaryPurple.copy(alpha = 0.5f)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
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
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = primaryPurple,
                unfocusedTextColor = primaryPurple,
                cursorColor = primaryPurple,
                focusedLabelColor = primaryPurple,
                unfocusedLabelColor = primaryPurple.copy(alpha = 0.5f)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onRegisterButtonClicked(username, email, password)
                authViewModel.register(username, email, password)
            },
            Modifier
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
                Text(
                    text = "Sign Up",
                    color = onPrimaryWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (authUiState) {
            is AuthUiState.Loading -> Text("Registering...", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            is AuthUiState.Error -> Text("Error: ${authUiState.message}", color = errorRed, style = MaterialTheme.typography.bodySmall)
            is AuthUiState.Success -> {
                Text("Register Success!", color = successGreen, style = MaterialTheme.typography.bodySmall)
                LaunchedEffect(Unit) {
                    onSignupSuccess(authUiState.token)
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account? Log in here",
            modifier = Modifier.padding(top = 16.dp)
                .clickable(
                    onClick = onLoginRouteClicked
                ),
            fontSize = 14.sp,
            color = primaryPurple
        )
    }
}