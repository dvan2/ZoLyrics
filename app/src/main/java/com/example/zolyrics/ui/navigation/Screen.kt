package com.example.zolyrics.ui.navigation

sealed class Screen (val route: String){
    object Home : Screen("home")
    object Favorites: Screen("favorites")
}