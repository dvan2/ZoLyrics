package com.example.zolyrics

import android.app.Application
import com.example.zolyrics.data.AppContainer
import com.example.zolyrics.data.AppDataContainer

class LyricsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}