package com.dvan.zolyrics.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.dvan.zolyrics.R

sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.label_home, Icons.Default.Home)
    object Favorites : Screen("favorites", R.string.label_favorites, Icons.Default.Favorite)
    object Sets : Screen("sets", R.string.label_sets, Icons.AutoMirrored.Filled.List)
    object SongDetail : Screen("song/{songId}", R.string.label_song, Icons.Default.Info) {
        fun createRoute(songId: String) = "song/$songId"
    }
    object SetDetail : Screen("sets/{setId}", R.string.label_set_details, Icons.Default.Info) {
        fun createRoute(setId: String) = "sets/$setId"
    }
    object Search : Screen("search", R.string.label_search, Icons.Default.Search)
}