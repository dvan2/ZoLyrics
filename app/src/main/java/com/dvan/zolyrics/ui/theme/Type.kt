package com.dvan.zolyrics.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    // Song titles (detail screen, card)
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),

    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),

    bodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),

    labelSmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
    )
)
