// routes/Routes.kt
package com.dvan.zolyrics.ui.navigation

object Routes {
    // Base routes (no args)
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val SETS = "sets"
    const val SET_CREATE = "sets/create"
    const val SEARCH = "search"

    // Argument keys
    const val ARG_SONG_ID = "songId"
    const val ARG_SET_ID = "setId"
    const val ARG_START_SONG_ID = "startSongId"

    // Detail/Runner routes (with placeholders)
    const val SONG_DETAIL = "song/{$ARG_SONG_ID}"
    const val SET_DETAIL = "sets/{$ARG_SET_ID}"

    // Optional query parameter pattern for runner
    // NOTE: keep the arg name in sync with ARG_START_SONG_ID
    const val SET_RUNNER = "sets/runner/{$ARG_SET_ID}?$ARG_START_SONG_ID={$ARG_START_SONG_ID}"

    // SavedState / other keys
    const val KEY_FAB_SAVE_REQUEST = "fab_save_request"

    // Builders (always prefer these vs string interpolation at call site)
    fun songDetail(songId: String) = "song/$songId"
    fun setDetail(setId: String) = "sets/$setId"
    fun setRunner(setId: String, startSongId: String? = null): String =
        if (startSongId.isNullOrBlank()) "sets/runner/$setId"
        else "sets/runner/$setId?$ARG_START_SONG_ID=$startSongId"
}

