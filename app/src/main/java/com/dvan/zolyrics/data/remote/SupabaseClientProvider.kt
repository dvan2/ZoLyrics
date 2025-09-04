package com.dvan.zolyrics.data.remote

import com.dvan.zolyrics.BuildConfig
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