package com.akilimo.rya

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import net.danlew.android.joda.JodaTimeAndroid

class Rya : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        JodaTimeAndroid.init(this)
    }
}
