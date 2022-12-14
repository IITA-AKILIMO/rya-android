package com.akilimo.rya.utils

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    private val editor: SharedPreferences.Editor
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, 0)

    companion object {
        private const val PREF_NAME = "rya-pref"
    }

    init {
        editor = pref.edit()
    }


    fun saveFilledProfileInfo(profileFilled: Boolean) {
        editor.putBoolean("endpoint", profileFilled)
        editor.commit()
    }

    fun isProfileInfoFilled(): Boolean {
        return pref.getBoolean("profile_filled", false)
    }

    fun saveApiEndpoint(endpoint: String) {
        editor.putString("endpoint", endpoint)
        editor.commit()
    }

    fun loadApiEndpoint(): String {
        return pref.getString("endpoint", "http://157.245.26.55:3000/api/")!!
    }
}
