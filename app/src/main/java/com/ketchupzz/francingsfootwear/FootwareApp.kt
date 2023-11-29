package com.ketchupzz.francingsfootwear

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp

class FootwareApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}