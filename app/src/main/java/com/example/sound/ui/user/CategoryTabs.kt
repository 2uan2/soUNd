package com.example.sound.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryTabs() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Playlists", "Albums", "Artists").forEach {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(it, color = Color.White)
            }
        }
    }
}
