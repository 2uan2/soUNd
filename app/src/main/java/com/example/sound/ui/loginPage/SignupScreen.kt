package com.example.sound.ui.loginPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.core.graphics.toColorInt
import com.example.sound.R

@Preview(showBackground = true)
@Composable
fun SignupScreen() {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color("#ffffff".toColorInt())),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id = R.drawable.wave),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Image(
            painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(150.dp)
        )

        Text(
            "Create an Account",
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color("#7d32a8".toColorInt())
        )

        var username by remember { mutableStateOf("Username") }
        var email by remember { mutableStateOf("Email Address") }
        var password by remember { mutableStateOf("Password") }
        var passwordVisible by remember { mutableStateOf(false) }

        // Username
        TextField(
            value = username, { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 64.dp, vertical = 8.dp)
                .border(1.dp, Color("#7d32a8".toColorInt()), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                color = Color("#7d32a8".toColorInt()),
                fontSize = 14.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color("#7d32a8".toColorInt()),
                unfocusedTextColor = Color("#7d32a8".toColorInt()),
                cursorColor = Color("#7d32a8".toColorInt())
            )
        )

        // Email
        TextField(
            value = email, { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 64.dp, vertical = 8.dp)
                .border(1.dp, Color("#7d32a8".toColorInt()), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                color = Color("#7d32a8".toColorInt()),
                fontSize = 14.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color("#7d32a8".toColorInt()),
                unfocusedTextColor = Color("#7d32a8".toColorInt()),
                cursorColor = Color("#7d32a8".toColorInt())
            )
        )

        // Password
        TextField(
            value = password, { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 64.dp, vertical = 8.dp)
                .border(1.dp, Color("#7d32a8".toColorInt()), shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                color = Color("#7d32a8".toColorInt()),
                fontSize = 14.sp
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color("#7d32a8".toColorInt()),
                unfocusedTextColor = Color("#7d32a8".toColorInt()),
                cursorColor = Color("#7d32a8".toColorInt())
            )
        )

        // Signup button
        Button(
            onClick = { /* Handle signup */ },
            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 64.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color("#7d32a8".toColorInt())),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Sign Up",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Already have an account? Log in here",
            Modifier.padding(top = 16.dp),
            fontSize = 14.sp,
            color = Color("#7d32a8".toColorInt())
        )
    }
}
