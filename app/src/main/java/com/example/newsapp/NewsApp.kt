package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.pref.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application() {

    companion object {
        lateinit var appContext: NewsApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
        appContext = this
    }
}