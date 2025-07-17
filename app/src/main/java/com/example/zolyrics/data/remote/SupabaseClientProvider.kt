package com.example.zolyrics.data.remote

import com.example.zolyrics.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    val client = createSupabaseClient (
        supabaseUrl = "https://wjqqqjngzurrjejrblfb.supabase.co",
        supabaseKey = BuildConfig.apiKeySafe
    ){
        install(Postgrest)
    }
}