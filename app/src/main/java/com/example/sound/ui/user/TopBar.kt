package com.example.sound.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme // Import MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton // For clickable icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // This can often be replaced by MaterialTheme.typography

@Composable
fun TopBar(username: String,) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Added padding for the whole row
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp) // Slightly larger avatar
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary), // Use primary color for avatar background
                contentAlignment = Alignment.Center
            ) {
                // Assuming 'N' is the first letter of the username.
                // In a real app, this might be dynamic or an image.
                Text(
                    text = username.firstOrNull()?.uppercaseChar().toString(),
                    color = MaterialTheme.colorScheme.onPrimary, // Use onPrimary for text on primary background
                    style = MaterialTheme.typography.titleMedium, // Use Material 3 typography
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp)) // Increased spacing
            Text(
                "HELLO , $username",
                color = MaterialTheme.colorScheme.onSurface, // Use onSurface for text color
                style = MaterialTheme.typography.headlineSmall, // Use Material 3 typography
                fontWeight = FontWeight.Bold
            )
        }
//        IconButton(onClick = { /* Handle search click */ }) { // Make search icon clickable
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search",
//                tint = MaterialTheme.colorScheme.onSurfaceVariant // Use a softer tint for icons
//            )
//        }
    }
}