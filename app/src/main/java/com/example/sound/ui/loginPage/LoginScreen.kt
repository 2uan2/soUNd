package com.example.sound.ui.loginPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun LoginScreen() {
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
            "Welcome to SoUNd",
            fontSize = 30.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color("#7d32a8".toColorInt())
        )

        var user by remember { mutableStateOf("Username") }
        var pass by remember { mutableStateOf("Password") }
        var passwordVisible by remember { mutableStateOf(false) }


        TextField(
            value = user, { text -> user = text },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp)
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
        TextField(
            value = pass, { text -> pass = text },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp)
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
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = {},
            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color("#7d32a8".toColorInt())),
            shape = RoundedCornerShape(50)

        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Don't remember password? click here",
            Modifier.padding(top = 8.dp, bottom = 16.dp),
            fontSize = 14.sp,
            color = Color("#7d32a8".toColorInt())
        )

        Row(Modifier.padding(top = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "",
                Modifier.padding(8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "",
                Modifier.padding(8.dp)
            )
        }
    }
}