package com.akilimo.rya

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.akilimo.rya.utils.PrefillCurrency
import net.danlew.android.joda.JodaTimeAndroid

class Rya : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        JodaTimeAndroid.init(this)

        PrefillCurrency(AppDatabase.getDatabase(this)!!.currencyDao()).fillWithStartingNotes(this)
    }
}
