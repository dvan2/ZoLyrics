package com.example.zolyrics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zolyrics.ui.theme.ZoLyricsTheme
import com.example.zolyrics.ui.ZoLyricsApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZoLyricsTheme {
                ZoLyricsApp()
            }
        }
    }
}
