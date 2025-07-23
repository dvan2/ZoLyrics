package com.example.zolyrics.ui.navigation

sealed class Screen (val route: String){
    object Home : Screen("home")
    object Favorites: Screen("favorites")
    object SongDetail: Screen("song/{songId}") {
        fun createRoute(songId: String) = "song/$songId"
    }
}