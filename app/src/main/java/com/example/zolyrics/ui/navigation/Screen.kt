package com.example.zolyrics.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Sets : Screen("sets", "Sets", Icons.Default.List) // ← Add this
    object SongDetail : Screen("song/{songId}", "", Icons.Default.Info) {
        fun createRoute(songId: String) = "song/$songId"
    }
}