package com.example.sound.data.database.model

data class RemoteSongDto(
    val id: Int,
    val name: String,
    val artist: String,
    val file: String, // URL đến bài hát
    val duration: Int,
    val cover_image: String?
)

fun RemoteSongDto.toLocalSong(): Song {
    return Song(
        songUri = this.file,
        name = this.name,
        artist = this.artist,
        duration = this.duration.toLong(),
        albumId = null, // Server không gửi, bạn có thể để null hoặc xử lý tùy yêu cầu
    )
}
