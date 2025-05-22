package com.example.sound.ui.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sound.ui.Screen

// Data class to represent a bottom bar item
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun MyBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            title = "Home",
            icon = Icons.Default.Home,
            screen = Screen.Home
        ),
        BottomNavItem(
            title = "Album",
            icon = Icons.Default.Album,
            screen = Screen.AlbumList
        ),
//        BottomNavItem(
//            title = "Player",
//            icon = Icons.Default.MusicNote,
//            screen = Screen.Player
//        ),
        BottomNavItem(
            title = "Playlist",
            icon = Icons.Default.LibraryMusic,
            screen = Screen.PlaylistList
        ),
        BottomNavItem(
            title = "Account",
            icon = Icons.Default.AccountCircle,
            screen = Screen.Account
        )
    )

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    NavigationBar(
        //test background
        containerColor = Color.Transparent, // 👈 Làm trong suốt
        tonalElevation = 0.dp // 👈 Loại bỏ đổ bóng (nếu cần)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.screen.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(text = item.title, style = MaterialTheme.typography.labelSmall)
                }
            )
        }
    }
}