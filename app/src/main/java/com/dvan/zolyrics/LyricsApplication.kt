package com.dvan.zolyrics

import android.app.Application
import com.dvan.zolyrics.data.AppContainer
import com.dvan.zolyrics.data.AppDataContainer

class LyricsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}