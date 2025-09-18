package com.dvan.zolyrics.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.dvan.zolyrics.R

//sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector? = null) {
//    object Home : Screen("home", R.string.label_home, Icons.Default.Home)
//    object Favorites : Screen("favorites", R.string.label_favorites, Icons.Default.Favorite)
//    object Sets : Screen("sets", R.string.label_sets, Icons.AutoMirrored.Filled.List)
//    object SongDetail : Screen("song/{songId}", R.string.label_song) {
//        fun createRoute(songId: String) = "song/$songId"
//    }
//    object SetDetail : Screen("sets/{setId}", R.string.label_set_details) {
//        fun createRoute(setId: String) = "sets/$setId"
//    }
//    object Search : Screen("search", R.string.label_search, Icons.Default.Search)
//}

// Screen.kt
sealed class Screen(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector? = null
) {
    object Home : Screen(Routes.HOME, R.string.label_home, Icons.Default.Home)
    object Favorites : Screen(Routes.FAVORITES, R.string.label_favorites, Icons.Default.Favorite)
    object Sets : Screen(Routes.SETS, R.string.label_sets, Icons.AutoMirrored.Filled.List)

    // Non-bottom-bar screens: no icon needed
    object SongDetail : Screen(Routes.SONG_DETAIL, R.string.label_song) {
        fun createRoute(songId: String) = Routes.songDetail(songId)
    }
    object SetDetail : Screen(Routes.SET_DETAIL, R.string.label_set_details) {
        fun createRoute(setId: String) = Routes.setDetail(setId)
    }
    object Search : Screen(Routes.SEARCH, R.string.label_search, Icons.Default.Search)
}
