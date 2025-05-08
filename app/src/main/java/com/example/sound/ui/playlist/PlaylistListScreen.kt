package com.example.sound.ui.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sound.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.sound.data.database.model.Playlist
import com.example.sound.data.database.model.PlaylistWithSongs
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card


@Composable
fun PlaylistListScreen(
    onPlaylistClicked: (Playlist) -> Unit,
    onCreatePlaylistButtonClicked: () -> Unit,
    viewModel: PlaylistListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp) // Thêm padding để không gian thoáng hơn
    ) {
        // Nút tạo playlist mới
        Button(
            onClick = onCreatePlaylistButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Padding dưới nút tạo playlist
        ) {
            Text(text = "Create Playlist")
        }

        // Danh sách Playlist
        LazyColumn(
            modifier = Modifier.fillMaxSize() // Làm cho LazyColumn chiếm toàn bộ không gian còn lại
        ) {
            items(uiState.playlists) { playlist ->
                PlaylistContainer(
                    playlistWithSongs = playlist,
                    onPlaylistClicked = onPlaylistClicked
                )
            }
        }
    }
}

@Composable
fun PlaylistContainer(
    onPlaylistClicked: (Playlist) -> Unit,
    playlistWithSongs: PlaylistWithSongs
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Padding giữa các Playlist
            .clickable {
                onPlaylistClicked(playlistWithSongs.playlist) // Khi nhấn vào Playlist, gọi hàm onPlaylistClicked
            },
//        elevation = 4.dp, // Thêm độ sâu cho Card
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp) // Tạo góc bo tròn
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding bên trong Card
        ) {
            // Hiển thị tên Playlist
            Text(
                text = playlistWithSongs.playlist.name,
                style = TextStyle(fontSize = 20.sp),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp) // Thêm padding dưới tên playlist
            )

            // Hiển thị số lượng bài hát trong Playlist
            Text(
                text = "Songs: ${playlistWithSongs.songs.size}",
                style = TextStyle(fontSize = 14.sp),
                color = Color.Gray
            )

//            // Nếu Playlist có hình ảnh, bạn có thể hiển thị hình ảnh đại diện
//            playlistWithSongs.playlist.image?.let { imageRes ->
//                Image(
//                    painter = painterResource(id = imageRes),
//                    contentDescription = "Playlist Image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp)
//                        .padding(top = 8.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
        }
    }
}
