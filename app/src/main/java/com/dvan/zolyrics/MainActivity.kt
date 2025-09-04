package com.dvan.zolyrics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dvan.zolyrics.ui.ZoLyricsApp
import com.dvan.zolyrics.ui.theme.ZoLyricsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dark = androidx.compose.foundation.isSystemInDarkTheme()
            ZoLyricsTheme(darkTheme = dark, dynamicColor = true) {
                ZoLyricsApp()
            }
        }
    }
}
