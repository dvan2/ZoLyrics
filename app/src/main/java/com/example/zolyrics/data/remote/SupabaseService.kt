package com.example.zolyrics.data.remote

import com.example.zolyrics.data.model.Song
import io.github.jan.supabase.postgrest.from


class SupabaseService {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllSongs(): List<Song> {
        return supabase.from("songs")
            .select()
            .decodeList()
    }
}