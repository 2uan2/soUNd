package com.example.sound.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color // Keep if needed for specific use, but prefer MaterialTheme.colorScheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // This can often be replaced by MaterialTheme.typography
import androidx.compose.material3.MaterialTheme // Import MaterialTheme
import androidx.compose.material3.Card // Use Card for list items for better visual separation
import androidx.compose.material3.CardDefaults // For Card colors and elevation
import androidx.compose.material.icons.Icons // For empty state icon
import androidx.compose.material.icons.filled.Info // For empty state icon (or similar)

data class LibraryItem(val title: String, val subtitle: String, val imageRes: Int)

@Composable
fun LibraryList(items: List<LibraryItem>) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding for the empty state message
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon( // Add an icon for the empty state
                    imageVector = Icons.Default.Info, // Or a more suitable icon like Icons.Default.FolderOff
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No items in your library.", // More descriptive text
                    style = MaterialTheme.typography.titleMedium, // Material 3 typography
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f) // Softer color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Start adding content to see it here.", // Helpful hint
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted padding
        ) {
            items(items) { item ->
                LibraryListItem(item = item) // Extract item composable
            }
        }
    }
}

@Composable
fun LibraryListItem(item: LibraryItem) {
    Card( // Use Card for each list item for better visual separation and depth
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium, // Consistent rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Subtle shadow
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh // Use a distinct surface color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // Adjusted internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp) // Slightly larger image size
                    .clip(RoundedCornerShape(12.dp)) // More rounded corners for images
            )
            Spacer(modifier = Modifier.width(16.dp)) // Increased spacing
            Column(modifier = Modifier.weight(1f)) { // Allow text to take remaining space
                Text(
                    item.title,
                    color = MaterialTheme.colorScheme.onSurface, // Use MaterialTheme colors
                    style = MaterialTheme.typography.titleMedium, // Material 3 typography
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1, // Prevent long titles from wrapping too much
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    item.subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Use MaterialTheme colors
                    style = MaterialTheme.typography.bodySmall, // Material 3 typography
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}