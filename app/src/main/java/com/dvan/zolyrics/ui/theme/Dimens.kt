package com.dvan.zolyrics.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class Spacing(
    val xs: androidx.compose.ui.unit.Dp = 4.dp,
    val sm: androidx.compose.ui.unit.Dp = 8.dp,
    val md: androidx.compose.ui.unit.Dp = 12.dp,
    val lg: androidx.compose.ui.unit.Dp = 16.dp,
    val xl: androidx.compose.ui.unit.Dp = 24.dp
)
data class Elevations(
    val level0: androidx.compose.ui.unit.Dp = 0.dp,
    val level1: androidx.compose.ui.unit.Dp = 2.dp,
    val level2: androidx.compose.ui.unit.Dp = 4.dp
)

val LocalSpacing   = staticCompositionLocalOf { Spacing() }
val LocalElevations = staticCompositionLocalOf { Elevations() }

