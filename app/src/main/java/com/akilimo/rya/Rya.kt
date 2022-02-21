package com.akilimo.rya

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

class Rya: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
