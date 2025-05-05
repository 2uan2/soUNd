package com.example.sound.data.network.model

import com.google.gson.annotations.SerializedName

data class SongUploadResponse(
    val id: Long,
    val name: String,
    val artist: String,
    val duration: Long,
    @SerializedName("file") val songUrl: String,
    @SerializedName("cover_image") val albumArt: String,
    @SerializedName("uploaded_at") val uploadedAt: String,
)

//@InternalSerializationApi @Serializable
//data class MarsPhoto(
//    val id: String,
//    @SerialName(value = "img_src")
//    val imgSrc: String
//)
