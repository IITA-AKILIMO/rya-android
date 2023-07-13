package com.akilimo.rya

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.akilimo.rya.utils.PrefillCurrency
import com.rollbar.android.Rollbar
import net.danlew.android.joda.JodaTimeAndroid

class Rya : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        JodaTimeAndroid.init(this)

        var env = "production"
        if (BuildConfig.DEBUG) {
            env = "development"
        }
        Rollbar.init(this, null, env)

        PrefillCurrency(AppDatabase.getDatabase(this)!!.currencyDao()).fillWithCountryCurrencies(
            this
        )
    }
}
