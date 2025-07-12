package com.example.zolyrics.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClientProvider {
    val client = createSupabaseClient (
        supabaseUrl = "hello",
        supabaseKey = "22-key"
    ){
        install(Postgrest)
    }
}