package com.akilimo.rya

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.akilimo.rya.utils.PrefillCurrency
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import net.danlew.android.joda.JodaTimeAndroid

class Rya : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        JodaTimeAndroid.init(this)

        PrefillCurrency(AppDatabase.getDatabase(this)!!.currencyDao()).fillWithCountryCurrencies(
            this
        )
    }
}
