package com.dvan.zolyrics.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteSongRow(
    val id: String,
    val title: String,
    val slug: String? = null,
    val key: String? = null,
    val bpm: Int? = null,
    @SerialName("artist_id") val artistId: String? = null,
    @SerialName("artist_name") val artistName: String? = null
)