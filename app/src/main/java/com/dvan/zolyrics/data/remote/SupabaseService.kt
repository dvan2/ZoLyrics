package com.dvan.zolyrics.data.remote

import com.dvan.zolyrics.data.model.LyricLine
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.data.remote.dto.RemoteSongRow
import io.github.jan.supabase.postgrest.from

class SupabaseService {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllSongs(): List<Song> {
        val rows: List<RemoteSongRow> = supabase
            .from("song_with_first_artist")
            .select()
            .decodeList()

        return rows.map { r ->
            Song(
                id = r.id,
                title = r.title,
                artistName = r.artistName.orEmpty(),
                bpm = r.bpm,
                key = r.key
            )
        }
    }

    suspend fun getLyrics(songId: String): List<LyricLine> {
        return supabase.from("lyric_lines")
            .select {
                filter {
                    eq("song_id", songId)
                }
            }.decodeList<LyricLine>()
    }
}