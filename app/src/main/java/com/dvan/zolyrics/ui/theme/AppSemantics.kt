package com.dvan.zolyrics.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppSemantics(
    val verseBg: Color,
    val chorusBg: Color,
    val bridgeBg: Color
) {
    companion object {
        fun from(colors: ColorScheme) = AppSemantics(
            verseBg  = colors.surfaceVariant.copy(alpha = 0.12f),
            chorusBg = colors.primary.copy(alpha = 0.10f),
            bridgeBg = colors.secondary.copy(alpha = 0.10f)
        )
    }
}

val LocalAppSemantics = staticCompositionLocalOf {
    // sensible defaults if someone forgets to wrap with theme
    AppSemantics(
        verseBg  = Color(0x11000000),
        chorusBg = Color(0x11000000),
        bridgeBg = Color(0x11000000)
    )
}

